package com.example.invest.jsbridge;


public interface WebViewJavascriptBridge {
	
	public void send(String data);
	public void send(String data, CallBackFunction responseCallback);
	
	

}
