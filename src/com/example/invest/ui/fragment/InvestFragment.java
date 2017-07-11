package com.example.invest.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.invest.R;
import com.example.invest.common.BaseFragment;
import com.example.invest.photo.view.PhotoViewActivity;
import com.example.invest.photo.view.photoView.PhotoView;
import com.example.invest.ui.activity.MapActivity;
import com.example.invest.utils.NetUtil;

public class InvestFragment extends BaseFragment implements OnClickListener{

	@Override
	public int getLayoutID() {
		return R.layout.fragment_invest;
	}
	
	@Override
	public void initView(View rootView) {
		Button btnCheck = (Button) rootView.findViewById(R.id.btn_check_net);
		btnCheck.setOnClickListener(this);
		Button btnMap = (Button) rootView.findViewById(R.id.btn_map);
		btnMap.setOnClickListener(this);
		rootView.findViewById(R.id.btn_photo_view).setOnClickListener(this);
	}

    private String[] items = {"http://img3.imgtn.bdimg.com/it/u=3355235453,2337163232&fm=21&gp=0.jpg",
            "http://img5.imgtn.bdimg.com/it/u=549778725,2120265236&fm=21&gp=0.jpg",
            "http://img3.imgtn.bdimg.com/it/u=3355235453,2337163232&fm=21&gp=0.jpg",
            "http://img5.imgtn.bdimg.com/it/u=549778725,2120265236&fm=21&gp=0.jpg"};
	
	@Override
	public void onClick(View arg0) {

		switch (arg0.getId()) {
		case R.id.btn_check_net:
			/**
			 * 检查网络状态，如果没有网就让用户设置网络。
			 */
			NetUtil.checkNetWorkState(mActivity);
			break;
		case R.id.btn_map:
			startActivity(new Intent(mActivity, MapActivity.class));
			break;
		case R.id.btn_photo_view:
			 // 1. new Intent
            Intent intent = new Intent(mActivity, PhotoViewActivity.class);
            List<String> bannersImages = new ArrayList<String>();
            for (int i = 0; i < items.length; i++) {
				bannersImages.add(items[i]);
			}

            // 2. 传递参数
            // 2.1 传当前位置
            intent.putExtra(PhotoViewActivity.CURRENT_POINT_KEY, 0);
            // 2.2 传总的图片路径  ArrayList
            intent.putStringArrayListExtra(PhotoViewActivity.IMAGE_URL_LIST_KEY, (ArrayList<String>) bannersImages);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
	


}
