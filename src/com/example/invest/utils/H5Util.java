package com.example.invest.utils;

import android.webkit.WebSettings;
import android.webkit.WebView;

public class H5Util {

	public static void load(WebView webView, String url) {
		WebSettings settings = webView.getSettings();

		settings.setBlockNetworkImage(false);
		settings.setAppCacheEnabled(true);

		// 设置支持javaScript脚步语言
		settings.setJavaScriptEnabled(true);

		settings.setBlockNetworkImage(false);// 解除图片阻塞
		settings.setAppCacheEnabled(true);// ，开启缓存

		// 支持双击-前提是页面要支持才显示
		settings.setUseWideViewPort(true);

		// 支持缩放按钮-前提是页面要支持才显示
		settings.setBuiltInZoomControls(true);

		// 设置客户端-不跳转到默认浏览器中
		// mWebView.setWebViewClient(new WebViewClient());
		webView.loadUrl(url);

	}

	/**
	 * 按键响应，在WebView中查看网页时，按返回键的时候按浏览历史退回,如果不做此项处理则整个WebView返回退出
	 */
	// @Override
	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// // Check if the key event was the Back button and if there's history
	// if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
	// // 返回键退回
	// mWebView.goBack();
	// return true;
	// }
	// // If it wasn't the Back key or there's no web page history, bubble up
	// // to the default
	// // system behavior (probably exit the activity)
	// return super.onKeyDown(keyCode, event);
	// }
}
