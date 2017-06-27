package com.example.invest.common;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment {

	public ActionBarActivity mActivity;
	public View mRootView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mActivity = (ActionBarActivity) getActivity();

		mRootView = inflater.inflate(getLayoutID(), container, false);

		initView(mRootView);
		return mRootView;
	}

	public abstract int getLayoutID();

	public void initView(View rootView) {

	}

	public void initData() {

	}
}
