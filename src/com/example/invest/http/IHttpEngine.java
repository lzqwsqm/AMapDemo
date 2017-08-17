package com.example.invest.http;

import java.util.Map;

import android.content.Context;

public interface IHttpEngine {
	// get请求
    void get(Context context,String url,Map<String,Object> params,ResponseCallback callBack);

    // post请求
    void post(Context context,String url,Map<String,Object> params,ResponseCallback callBack);

    // 下载文件


    // 上传文件


    // https 添加证书
}
