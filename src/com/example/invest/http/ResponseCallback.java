package com.example.invest.http;

import java.util.Map;

import com.example.invest.ui.activity.MapActivity;

import android.content.Context;

/**
 * »Øµ÷
 * @author admin
 *
 */
public interface ResponseCallback {
	void onPreExecute(Context context,Map<String, Object> params);
	void onSuccess(String result);
	void onError(Exception e);

}
