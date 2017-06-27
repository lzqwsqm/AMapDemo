package com.example.invest.ui.activity;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Interpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.animation.TranslateAnimation;
import com.amap.api.services.core.LatLonPoint;
import com.example.invest.R;
import com.example.invest.location.SensorEventHelper;

/**
 * ��λͼ���ͷָ���ֻ�����
 */
public class MapActivity extends ActionBarActivity implements LocationSource,
		AMapLocationListener {
	private AMap aMap;
	private MapView mapView;
	private OnLocationChangedListener mListener;
	private AMapLocationClient mlocationClient;
	private AMapLocationClientOption mLocationOption;

	private TextView mLocationErrText;
	private boolean mFirstFix = false;
	public static final String LOCATION_MARKER_FLAG = "mylocation";
	private Marker locationMarker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// ����ʾ����ı�����
		setContentView(R.layout.activity_map);
		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);// �˷���������д
		init();
	}

	/**
	 * ��ʼ��
	 */
	private void init() {
		if (aMap == null) {
			aMap = mapView.getMap();
			setUpMap();
		}

		mLocationErrText = (TextView) findViewById(R.id.location_errInfo_text);
		mLocationErrText.setVisibility(View.GONE);

		 aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
	            @Override
	            public void onCameraChange(CameraPosition cameraPosition) {

	            }

	            @Override
	            public void onCameraChangeFinish(CameraPosition cameraPosition) {
	            	startJumpAnimation();
	            }
	        });
		
		
		aMap.setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
			@Override
			public void onMapLoaded() {
				addMarkerInScreenCenter(null);
			}
		});
	}

	/**
	 * ����һЩamap������
	 */
	private void setUpMap() {

        aMap.getUiSettings().setZoomControlsEnabled(false);
		aMap.setLocationSource(this);// ���ö�λ����
		aMap.getUiSettings().setMyLocationButtonEnabled(true);// ����Ĭ�϶�λ��ť�Ƿ���ʾ
		aMap.setMyLocationEnabled(true);// ����Ϊtrue��ʾ��ʾ��λ�㲢�ɴ�����λ��false��ʾ���ض�λ�㲢���ɴ�����λ��Ĭ����false
		// ���ö�λ������Ϊ��λģʽ �������ɶ�λ��������ͼ������������ת����
		aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
		
		MyLocationStyle myLocationStyle = new MyLocationStyle();//��ʼ����λ������ʽ��myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//������λ���ҽ��ӽ��ƶ�����ͼ���ĵ㣬��λ�������豸������ת�����һ�����豸�ƶ�����1��1�ζ�λ�����������myLocationType��Ĭ��Ҳ��ִ�д���ģʽ��
		/**
		 * ����ʾĬ��ԲȦ
		 */
		myLocationStyle.strokeWidth(0);
		myLocationStyle.radiusFillColor(Color.TRANSPARENT);
		myLocationStyle.interval(2000); //����������λģʽ�µĶ�λ�����ֻ��������λģʽ����Ч�����ζ�λģʽ�²�����Ч����λΪ���롣
		myLocationStyle.showMyLocation(true);//����ʾĬ����ɫ��
		myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.navi_map_gps_locked));
		myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);
		aMap.setMyLocationStyle(myLocationStyle);//���ö�λ�����Style
		
	}

	/**
	 * ����������д
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	/**
	 * ����������д
	 */
	@Override
	protected void onPause() {
		super.onPause();

		mapView.onPause();
		deactivate();
		mFirstFix = false;
	}

	/**
	 * ����������д
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	/**
	 * ����������д
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();

		mapView.onDestroy();
		if (null != mlocationClient) {
			mlocationClient.onDestroy();
		}
	}

	/**
	 * ��λ�ɹ���ص�����
	 */
	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		if (mListener != null && amapLocation != null) {
			if (amapLocation != null && amapLocation.getErrorCode() == 0) {
mListener.onLocationChanged(amapLocation);
				mLocationErrText.setVisibility(View.GONE);
				LatLng location = new LatLng(amapLocation.getLatitude(),
						amapLocation.getLongitude());
				if (!mFirstFix) {
					mFirstFix = true;
					aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,
							16f));
				} else {
//					mLocMarker.setPosition(location);
					aMap.moveCamera(CameraUpdateFactory.changeLatLng(location));
				}
			} else {
				String errText = "��λʧ��," + amapLocation.getErrorCode() + ": "
						+ amapLocation.getErrorInfo();
				Log.e("AmapErr", errText);
				mLocationErrText.setVisibility(View.VISIBLE);
				mLocationErrText.setText(errText);
			}
		}
	}

	/**
	 * ���λ
	 */
	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
		if (mlocationClient == null) {
			mlocationClient = new AMapLocationClient(this);
			mLocationOption = new AMapLocationClientOption();
			// ���ö�λ����
			mlocationClient.setLocationListener(this);
			// ����Ϊ�߾��ȶ�λģʽ
			mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
			mLocationOption.setOnceLocation(true);
			// ���ö�λ����
			mlocationClient.setLocationOption(mLocationOption);
			// �˷���Ϊÿ���̶�ʱ��ᷢ��һ�ζ�λ����Ϊ�˼��ٵ������Ļ������������ģ�
			// ע�����ú��ʵĶ�λʱ��ļ������С���֧��Ϊ2000ms���������ں���ʱ�����stopLocation()������ȡ����λ����
			// �ڶ�λ�������ں��ʵ��������ڵ���onDestroy()����
			// �ڵ��ζ�λ����£���λ���۳ɹ���񣬶��������stopLocation()�����Ƴ����󣬶�λsdk�ڲ����Ƴ�
			mlocationClient.startLocation();
		}
	}

	/**
	 * ֹͣ��λ
	 */
	@Override
	public void deactivate() {
		mListener = null;
		if (mlocationClient != null) {
			mlocationClient.stopLocation();
			mlocationClient.onDestroy();
		}
		mlocationClient = null;
	}



	/**
	 * ��ͼ���ĵ����marker
	 * 
	 * @param locationLatLng
	 */
	private void addMarkerInScreenCenter(LatLng locationLatLng) {
		LatLng latLng = aMap.getCameraPosition().target;
		Point screenPosition = aMap.getProjection().toScreenLocation(latLng);
		locationMarker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.ic_launcher)));
		// ����Marker����Ļ��,�������ͼ�ƶ�
		locationMarker.setPositionByPixels(screenPosition.x, screenPosition.y);
		locationMarker.setZIndex(1);

	}

	// dip��pxת��
	private static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * ��Ļ����marker ����
	 */
	public void startJumpAnimation() {

		if (locationMarker != null) {
			// ������Ļ���������Ҫ�ƶ���Ŀ���
			final LatLng latLng = locationMarker.getPosition();
			Point point = aMap.getProjection().toScreenLocation(latLng);
			point.y -= dip2px(this, 125);
			LatLng target = aMap.getProjection().fromScreenLocation(point);
			// ʹ��TranslateAnimation,��дһ����Ҫ�ƶ���Ŀ���
			TranslateAnimation animation = new TranslateAnimation(target);
			animation.setInterpolator(new Interpolator() {
				@Override
				public float getInterpolation(float input) {
					// ģ���ؼ��ٶȵ�interpolator
					if (input <= 0.5) {
						return (float) (0.5f - 2 * (0.5 - input)
								* (0.5 - input));
					} else {
						return (float) (0.5f - Math.sqrt((input - 0.5f)
								* (1.5f - input)));
					}
				}
			});
			// �����ƶ�����Ҫ��ʱ��
			animation.setDuration(600);
			// ���ö���
			locationMarker.setAnimation(animation);
			// ��ʼ����
			locationMarker.startAnimation();

		} else {
			Log.e("ama", "screenMarker is null");
		}
	}

	private void hideSoftKey(View view) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

}