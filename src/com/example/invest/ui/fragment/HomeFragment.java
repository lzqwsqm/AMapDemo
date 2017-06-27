package com.example.invest.ui.fragment;

import android.annotation.SuppressLint;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.invest.R;
import com.example.invest.common.BaseFragment;
import com.example.invest.utils.H5Util;

@SuppressLint("SetJavaScriptEnabled")
public class HomeFragment extends BaseFragment {

	private WebView mWebView;
	private String url = "http://www.sofamovie.cn/html/mobile/Mandroid0721.html";

	@Override
	public int getLayoutID() {
		return R.layout.fragment_home;
	}

	@Override
	public void initView(View rooView) {
		super.initView(rooView);
		mWebView = (WebView) rooView.findViewById(R.id.webview);
		
		H5Util.load(mWebView, url);

	}
	
	
	

	
}
