package com.example.invest.common;

import java.util.Stack;

import android.app.Activity;

/**
 * activity的管理类
 * @author admin
 *
 */
public class ActivityManager {
	private Stack<Activity> mActivityStack = new Stack<Activity>();

	private static ActivityManager sActivityManager = null;

	private ActivityManager() {
	};

	public static ActivityManager getInstance() {
		if (sActivityManager == null) {
			synchronized (ActivityManager.class) {
				if (sActivityManager == null) {
					sActivityManager = new ActivityManager();
				}
			}
		}
		return sActivityManager;
	}

	public void add(Activity activity) {
		mActivityStack.add(activity);
	}

	public void remove(Activity activity) {
		for (Activity temp : mActivityStack) {
			if (temp.getClass().equals(activity.getClass())) {
				temp.finish();
				mActivityStack.remove(activity);
				break;
			}
		}
	}

	public void removeAll(){
		for (Activity tempActivity : mActivityStack) {
			tempActivity.finish();
			mActivityStack.remove(tempActivity);
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
