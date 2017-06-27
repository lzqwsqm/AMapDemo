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
 * 定位图标箭头指向手机朝向
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
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 不显示程序的标题栏
		setContentView(R.layout.activity_map);
		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);// 此方法必须重写
		init();
	}

	/**
	 * 初始化
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
	 * 设置一些amap的属性
	 */
	private void setUpMap() {

        aMap.getUiSettings().setZoomControlsEnabled(false);
		aMap.setLocationSource(this);// 设置定位监听
		aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
		aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
		// 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
		aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
		
		MyLocationStyle myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
		/**
		 * 不显示默认圆圈
		 */
		myLocationStyle.strokeWidth(0);
		myLocationStyle.radiusFillColor(Color.TRANSPARENT);
		myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
		myLocationStyle.showMyLocation(true);//不显示默认蓝色点
		myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.navi_map_gps_locked));
		myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);
		aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
		
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		super.onPause();

		mapView.onPause();
		deactivate();
		mFirstFix = false;
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	/**
	 * 方法必须重写
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
	 * 定位成功后回调函数
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
				String errText = "定位失败," + amapLocation.getErrorCode() + ": "
						+ amapLocation.getErrorInfo();
				Log.e("AmapErr", errText);
				mLocationErrText.setVisibility(View.VISIBLE);
				mLocationErrText.setText(errText);
			}
		}
	}

	/**
	 * 激活定位
	 */
	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
		if (mlocationClient == null) {
			mlocationClient = new AMapLocationClient(this);
			mLocationOption = new AMapLocationClientOption();
			// 设置定位监听
			mlocationClient.setLocationListener(this);
			// 设置为高精度定位模式
			mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
			mLocationOption.setOnceLocation(true);
			// 设置定位参数
			mlocationClient.setLocationOption(mLocationOption);
			// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
			// 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
			// 在定位结束后，在合适的生命周期调用onDestroy()方法
			// 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
			mlocationClient.startLocation();
		}
	}

	/**
	 * 停止定位
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
	 * 地图中心点添加marker
	 * 
	 * @param locationLatLng
	 */
	private void addMarkerInScreenCenter(LatLng locationLatLng) {
		LatLng latLng = aMap.getCameraPosition().target;
		Point screenPosition = aMap.getProjection().toScreenLocation(latLng);
		locationMarker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.ic_launcher)));
		// 设置Marker在屏幕上,不跟随地图移动
		locationMarker.setPositionByPixels(screenPosition.x, screenPosition.y);
		locationMarker.setZIndex(1);

	}

	// dip和px转换
	private static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 屏幕中心marker 跳动
	 */
	public void startJumpAnimation() {

		if (locationMarker != null) {
			// 根据屏幕距离计算需要移动的目标点
			final LatLng latLng = locationMarker.getPosition();
			Point point = aMap.getProjection().toScreenLocation(latLng);
			point.y -= dip2px(this, 125);
			LatLng target = aMap.getProjection().fromScreenLocation(point);
			// 使用TranslateAnimation,填写一个需要移动的目标点
			TranslateAnimation animation = new TranslateAnimation(target);
			animation.setInterpolator(new Interpolator() {
				@Override
				public float getInterpolation(float input) {
					// 模拟重加速度的interpolator
					if (input <= 0.5) {
						return (float) (0.5f - 2 * (0.5 - input)
								* (0.5 - input));
					} else {
						return (float) (0.5f - Math.sqrt((input - 0.5f)
								* (1.5f - input)));
					}
				}
			});
			// 整个移动所需要的时间
			animation.setDuration(600);
			// 设置动画
			locationMarker.setAnimation(animation);
			// 开始动画
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