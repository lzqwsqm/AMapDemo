package com.example.invest.http;

import java.util.Map;

import android.content.Context;

public interface IHttpEngine {
	// get����
    void get(Context context,String url,Map<String,Object> params,ResponseCallback callBack);

    // post����
    void post(Context context,String url,Map<String,Object> params,ResponseCallback callBack);

    // �����ļ�


    // �ϴ��ļ�


    // https ���֤��
}
