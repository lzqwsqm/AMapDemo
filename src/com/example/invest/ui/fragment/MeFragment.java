package com.example.invest.ui.fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.invest.R;
import com.example.invest.common.BaseFragment;

public class MeFragment extends BaseFragment implements OnClickListener {

	private TextView chronometer, date;

	private Button btnStart, btnStop;

	private long between;

	private Timer timer;

	@Override
	public int getLayoutID() {
		return R.layout.fragment_me;
	}

	@Override
	public void initView(View rootView) {
		super.initView(rootView);
		chronometer = (TextView) rootView.findViewById(R.id.time);
		date = (TextView) rootView.findViewById(R.id.date);
		btnStart = (Button) rootView.findViewById(R.id.start);
		btnStop = (Button) rootView.findViewById(R.id.stop);

		timer = new Timer();
		btnStart.setOnClickListener(this);
		btnStop.setOnClickListener(this);
		time();
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.start:
			TimerTask task = new TimerTask() {
				@Override
				public void run() {
					mActivity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							between++;
							// long day1 = between / (24 * 3600);
							// long hour1 = between % (24 * 3600) / 3600;
							long minute1 = between % 3600 / 60;
							long second1 = between % 60;
							// System.out.println("" + day1 + "��" + hour1 + "Сʱ"
							// + minute1 + "��" + second1 + "��");
							chronometer
									.setText(minute1
											+ ":"
											+ (String.valueOf(second1).length() == 1 ? "0"
													+ second1
													: second1));
						}
					});
				}
			};
			timer.schedule(task, 0, 1000); // ��ʱ1000ms��ִ�У�1000msִ��һ��
			break;
		case R.id.stop:
			timer.cancel(); // �˳���ʱ��
			break;

		default:
			break;
		}
	}

	// ��ǰʱ���ĳ��ʱ����ʱ���
	long time = 0;

	/**
	 * ����ĳ��ʱ���Ķ೤ʱ��
	 */
	public void time() {
		SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		java.util.Date begin = null;
		try {
			begin = dfs.parse("2017-07-11 23:07:24");

			java.util.Date end = new Date();// ��ǰʱ��
			time = (end.getTime() - begin.getTime()) / 1000;
		} catch (ParseException e) {
			e.printStackTrace();
		}

		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				mActivity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						time++;
						long minute1 = time % 3600 / 60;
						long second1 = time % 60;
						date.setText("�ѵȴ�"
								+ minute1
								+ "��"
								+ (String.valueOf(second1).length() == 1 ? "0"
										+ second1 : second1) + "��");
					}
				});
			}
		};
		timer.schedule(task, 0, 1000); // ��ʱ1000ms��ִ�У�1000msִ��һ��
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (timer != null) {
			timer.cancel();
		}
	}

}
