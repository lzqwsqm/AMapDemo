package com.example.invest.utils;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.example.invest.common.Constants;

/**
 * �������
 * 
 * @author admin
 * 
 */
public class NetUtil {

	/**
	 * ��������Ƿ�����
	 * 
	 * @return
	 */
	public static boolean checkNetWorkState(Context context) {
		boolean flag = false;
		// �õ�����������Ϣ
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		// �ж������Ƿ�����
		if (manager.getActiveNetworkInfo() != null) {
			flag = manager.getActiveNetworkInfo().isAvailable();
		}
		if (flag) {
			// �ж���������
			isNetworkAvailable(context, manager);
		} else {
			// ������δ���ӣ��򵯳���ʾ��������
			setNetWork(context);
		}
		return flag;
	}

	/*
	 * �����Ѿ����ӣ��ж��� Wi-Fi �����ƶ����磨2G��3G��4G��
	 * 
	 * @return
	 */
	private static void isNetworkAvailable(Context context,
			ConnectivityManager manager) {
		NetworkInfo.State gprs = manager.getNetworkInfo(
				ConnectivityManager.TYPE_MOBILE).getState();
		NetworkInfo.State wifi = manager.getNetworkInfo(
				ConnectivityManager.TYPE_WIFI).getState();
		if (gprs == NetworkInfo.State.CONNECTED
				|| gprs == NetworkInfo.State.CONNECTING) {
			Log.e("GPRS", "�ƶ������Ѵ򿪣�");
		}
		if (wifi == NetworkInfo.State.CONNECTED
				|| wifi == NetworkInfo.State.CONNECTING) {
			Log.e("WIFI", "Wi-Fi�Ѵ򿪣�");
		}

		// ��ȡ��������
		switch (getNetWorkType(context)) {
		case Constants.NETTYPE_2G:
			ToastUtils.showShort(context, "��ǰ������2G����");
			break;
		case Constants.NETTYPE_3G:
			ToastUtils.showShort(context, "��ǰ������3G����");
			break;
		case Constants.NETTYPE_4G:
			ToastUtils.showShort(context, "��ǰ������4G����");
			break;
		case Constants.NETTYPE_WIFI:
			ToastUtils.showShort(context, "��ǰ������wifi");
			break;
		}

	}

	/**
	 * ��ȡ��������
	 * 
	 * @param context
	 *            Context
	 * @return true ��ʾ�������
	 */
	private static int getNetWorkType(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager.getActiveNetworkInfo();

		if (networkInfo != null && networkInfo.isConnected()) {
			String type = networkInfo.getTypeName();

			if (type.equalsIgnoreCase("WIFI")) {
				return Constants.NETTYPE_WIFI;// Wi-Fi����
			} else if (type.equalsIgnoreCase("MOBILE")) {
				NetworkInfo mobileInfo = manager
						.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
				if (mobileInfo != null) {
					switch (mobileInfo.getType()) {
					case ConnectivityManager.TYPE_MOBILE:// �ֻ�����
						switch (mobileInfo.getSubtype()) {
						// --------------------Added in API level
						// 1---------------------
						// (3G)��ͨ ~ 400-7000 kbps
						case TelephonyManager.NETWORK_TYPE_UMTS:
							// (2.5G) �ƶ�����ͨ ~ 100 kbps
						case TelephonyManager.NETWORK_TYPE_GPRS:
							// (2.75G) 2.5G �� 3G �Ĺ��� �ƶ�����ͨ ~ 50-100 kbps
						case TelephonyManager.NETWORK_TYPE_EDGE:
							// -----------------Added in API level
							// 4---------------------
							// ( 3G )���� ~ 400-1000 kbps
						case TelephonyManager.NETWORK_TYPE_EVDO_0:
							// (2G ����) ~ 14-64 kbps
						case TelephonyManager.NETWORK_TYPE_CDMA:
							// (3.5G) ����3G���� ~ 600-1400 kbps
						case TelephonyManager.NETWORK_TYPE_EVDO_A:
							// ( 2G ) ~ 50-100 kbps
						case TelephonyManager.NETWORK_TYPE_1xRTT:
							// ---------------------Added in API level
							// 5--------------------
							// (3.5G ) ~ 2-14 Mbps
						case TelephonyManager.NETWORK_TYPE_HSDPA:
							// ( 3.5G ) ~ 1-23 Mbps
						case TelephonyManager.NETWORK_TYPE_HSUPA:
							// ( 3G ) ��ͨ ~ 700-1700 kbps
						case TelephonyManager.NETWORK_TYPE_HSPA:
							// ---------------------Added in API level
							// 8---------------------
							// (2G ) ~25 kbps
						case TelephonyManager.NETWORK_TYPE_IDEN:
							return Constants.NETTYPE_2G;
							// ---------------------Added in API level
							// 9---------------------
							// 3G-3.5G ~ 5 Mbps
						case TelephonyManager.NETWORK_TYPE_EVDO_B:
							// ---------------------Added in API level
							// 11--------------------
							// (4G) ~ 10+ Mbps
						case TelephonyManager.NETWORK_TYPE_LTE:
							return Constants.NETTYPE_4G;
							// 3G(3G��4G����������) ~ 1-2 Mbps
						case TelephonyManager.NETWORK_TYPE_EHRPD:
							// --------------------Added in API level
							// 13-------------------
							// ( 3G ) ~ 10-20 Mbps
						case TelephonyManager.NETWORK_TYPE_HSPAP:
							return Constants.NETTYPE_3G;
							// ������
						default:
							return Constants.NETTYPE_NONE;
						}
					}
				}
			}
		}
		return Constants.NETTYPE_NONE;
	}

	/**
	 * ����Ϊ����ʱ����������
	 */
	private static void setNetWork(final Context context) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("����������ʾ").setMessage("���粻���ã��������������������");
		builder.setPositiveButton("����", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = null;
				/**
				 * �ж��ֻ�ϵͳ�İ汾����� API ���� 10 ���� 3.0+ ��Ϊ 3.0 ���ϵİ汾�����ú� 3.0
				 * ���µ����ò�һ�������õķ�����ͬ
				 */
				if (Build.VERSION.SDK_INT > 10) {
					intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
				} else {
					intent = new Intent();
					ComponentName componentName = new ComponentName(
							"com.android.settings",
							"com.android.settings.WirelessSettings");
					intent.setComponent(componentName);
					intent.setAction("android.intent.action.VIEW");
				}
				// ������ Wi-Fi ����ҳ��
				context.startActivity(intent);
			}
		});

		builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				return;
			}
		});

		builder.create();
		builder.show();
	}
}
