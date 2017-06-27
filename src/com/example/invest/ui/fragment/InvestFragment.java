package com.example.invest.ui.fragment;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.invest.R;
import com.example.invest.common.BaseFragment;
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
	}

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
		default:
			break;
		}
	}
	


}
