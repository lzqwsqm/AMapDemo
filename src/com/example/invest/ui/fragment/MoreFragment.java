package com.example.invest.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.widget.Button;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.alibaba.fastjson.support.spring.FastJsonJsonView;
import com.amap.api.col.u;
import com.example.invest.R;
import com.example.invest.common.BaseFragment;
import com.example.invest.jsbridge.BridgeHandler;
import com.example.invest.jsbridge.BridgeWebView;
import com.example.invest.jsbridge.DefaultHandler;
import com.example.invest.jsbridge.CallBackFunction;

public class MoreFragment extends BaseFragment implements OnClickListener {

	private final String TAG = "MainActivity";

	BridgeWebView webView;

	Button button;

	@Override
	public int getLayoutID() {
		return R.layout.fragment_more;
	}

	int RESULT_CODE = 0;

	ValueCallback<Uri> mUploadMessage;

	static class Location {
		String address;
	}

	static class User {
		String name;
		Location location;
		String testStr;
		@Override
		public String toString() {
			return "User [name=" + name + ", location=" + location
					+ ", testStr=" + testStr + "]";
		}
		
	
		
	}

	@Override
	public void initView(View rootView) {
		super.initView(rootView);
		webView = (BridgeWebView) rootView.findViewById(R.id.webView);

		button = (Button) rootView.findViewById(R.id.button);

		button.setOnClickListener(this);

		webView.setDefaultHandler(new DefaultHandler());

		webView.setWebChromeClient(new WebChromeClient() {

			@SuppressWarnings("unused")
			public void openFileChooser(ValueCallback<Uri> uploadMsg,
					String AcceptType, String capture) {
				this.openFileChooser(uploadMsg);
			}

			@SuppressWarnings("unused")
			public void openFileChooser(ValueCallback<Uri> uploadMsg,
					String AcceptType) {
				this.openFileChooser(uploadMsg);
			}

			public void openFileChooser(ValueCallback<Uri> uploadMsg) {
				mUploadMessage = uploadMsg;
				pickFile();
			}
		});

		webView.loadUrl("file:///android_asset/demo.html");

		webView.registerHandler("submitFromWeb", new BridgeHandler() {

			@Override
			public void handler(String data, CallBackFunction function) {
				Log.i(TAG, "handler = submitFromWeb, data from web = " + data);
				function.onCallBack("submitFromWeb exe, response data ���� from Java");
			}

		});

		User user = new User();
		Location location = new Location();
		location.address = "SDU";
		user.location = location;
		user.name = "��ͷ��";

		webView.callHandler("functionInJs",user.toString(),
				new CallBackFunction() {
					@Override
					public void onCallBack(String data) {

					}
				});

		webView.send("hello");

	}

	public void pickFile() {
		Intent chooserIntent = new Intent(Intent.ACTION_GET_CONTENT);
		chooserIntent.setType("image/*");
		startActivityForResult(chooserIntent, RESULT_CODE);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == RESULT_CODE) {
			if (null == mUploadMessage) {
				return;
			}
			Uri result = intent == null || resultCode != RESULT_CODE ? null
					: intent.getData();
			mUploadMessage.onReceiveValue(result);
			mUploadMessage = null;
		}
	}

	@Override
	public void onClick(View v) {
		if (button.equals(v)) {
			webView.callHandler("functionInJs", "data from Java",
					new CallBackFunction() {

						@Override
						public void onCallBack(String data) {
							// TODO Auto-generated method stub
							Log.i(TAG, "reponse data from js " + data);
							Toast.makeText(mActivity, data, Toast.LENGTH_SHORT).show();
						}

					});
		}

	}

}