package com.example.invest.common;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

public abstract class BaseActivity extends ActionBarActivity {

	public ActionBar mActionBar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityManager.getInstance().add(this);
		setContentView(initLayoutResID());
		mActionBar = getSupportActionBar();
		initActionBar(mActionBar);
		initView();
		initData();

	}

	public abstract int initLayoutResID();

	public abstract void initActionBar(ActionBar actionBar);

	public void initView() {

	}

	public void initData() {

	}

	/******* 一些工具 ***********************************/
	public void closeCurrent() {
		ActivityManager.getInstance().remove(this);
	}

	public void showToast(String text) {
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}
	
    public void jump2Activity(Class<?> clazz, Bundle bundle) {
        Intent it = new Intent(this, clazz);
        if (bundle != null) {
            it.putExtra("param", bundle);
        }
        startActivity(it);
    }
	

}
