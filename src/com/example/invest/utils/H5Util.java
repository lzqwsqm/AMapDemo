package com.example.invest.utils;

import android.webkit.WebSettings;
import android.webkit.WebView;

public class H5Util {

	public static void load(WebView webView, String url) {
		WebSettings settings = webView.getSettings();

		settings.setBlockNetworkImage(false);
		settings.setAppCacheEnabled(true);

		// ����֧��javaScript�Ų�����
		settings.setJavaScriptEnabled(true);

		settings.setBlockNetworkImage(false);// ���ͼƬ����
		settings.setAppCacheEnabled(true);// ����������

		// ֧��˫��-ǰ����ҳ��Ҫ֧�ֲ���ʾ
		settings.setUseWideViewPort(true);

		// ֧�����Ű�ť-ǰ����ҳ��Ҫ֧�ֲ���ʾ
		settings.setBuiltInZoomControls(true);

		// ���ÿͻ���-����ת��Ĭ���������
		// mWebView.setWebViewClient(new WebViewClient());
		webView.loadUrl(url);

	}

	/**
	 * ������Ӧ����WebView�в鿴��ҳʱ�������ؼ���ʱ�������ʷ�˻�,������������������WebView�����˳�
	 */
	// @Override
	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// // Check if the key event was the Back button and if there's history
	// if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
	// // ���ؼ��˻�
	// mWebView.goBack();
	// return true;
	// }
	// // If it wasn't the Back key or there's no web page history, bubble up
	// // to the default
	// // system behavior (probably exit the activity)
	// return super.onKeyDown(keyCode, event);
	// }
}
