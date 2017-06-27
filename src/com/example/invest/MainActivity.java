package com.example.invest;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.example.invest.common.ActivityManager;
import com.example.invest.common.BaseActivity;
import com.example.invest.ui.fragment.HomeFragment;
import com.example.invest.ui.fragment.InvestFragment;
import com.example.invest.ui.fragment.MeFragment;
import com.example.invest.ui.fragment.MoreFragment;

public class MainActivity extends BaseActivity {

	private FrameLayout flContent;

	private RadioButton tvHome;
	private RadioButton tvInvest;
	private RadioButton tvMe;
	private RadioButton tvMore;

	private RadioGroup rgGroup;

	private HomeFragment mHomeFragment;
	private InvestFragment mInvestFragment;
	private MeFragment mMeFragment;
	private MoreFragment mMoreFragment;

	@Override
	public int initLayoutResID() {
		return R.layout.activity_main;
	}

	@Override
	public void initActionBar(ActionBar actionBar) {

	}

	@Override
	public void initView() {
		flContent = (FrameLayout) findViewById(R.id.fl_activity_main_content);
		tvHome = (RadioButton) findViewById(R.id.tv_main_bottom_home);
		tvInvest = (RadioButton) findViewById(R.id.tv_main_bottom_invest);
		tvMe = (RadioButton) findViewById(R.id.tv_main_bottom_me);
		tvMore = (RadioButton) findViewById(R.id.tv_main_bottom_more);

		rgGroup = (RadioGroup) findViewById(R.id.rg_main_bottom_group);
	}

	private FragmentTransaction mTransaction;

	@Override
	public void initData() {
		rgGroup.check(tvHome.getId());
		if (mHomeFragment == null) {
			mHomeFragment = new HomeFragment();
		}
		getSupportFragmentManager().beginTransaction()
				.add(R.id.fl_activity_main_content, mHomeFragment)
				.show(mHomeFragment).commit();

		mActionBar.setTitle("首页");
		rgGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {

				FragmentManager manager = MainActivity.this
						.getSupportFragmentManager();
				mTransaction = manager.beginTransaction();
				hideFragment();
				switch (arg1) {
				case R.id.tv_main_bottom_home:
					if (mHomeFragment == null) {
						mHomeFragment = new HomeFragment();
						mTransaction.add(R.id.fl_activity_main_content,
								mHomeFragment);
					}
					mTransaction.show(mHomeFragment);
					mActionBar.setTitle("首页");

					break;
				case R.id.tv_main_bottom_invest:
					if (mInvestFragment == null) {
						mInvestFragment = new InvestFragment();
						mTransaction.add(R.id.fl_activity_main_content,
								mInvestFragment);
					}
					mTransaction.show(mInvestFragment);

					mActionBar.setTitle("我要投资");
					break;
				case R.id.tv_main_bottom_me:
					if (mMeFragment == null) {
						mMeFragment = new MeFragment();
						mTransaction.add(R.id.fl_activity_main_content,
								mMeFragment);
					}
					mTransaction.show(mMeFragment);

					mActionBar.setTitle("我的资产");
					break;
				case R.id.tv_main_bottom_more:
					if (mMoreFragment == null) {
						mMoreFragment = new MoreFragment();
						mTransaction.add(R.id.fl_activity_main_content,
								mMoreFragment);
					}
					mTransaction.show(mMoreFragment);

					mActionBar.setTitle("更多");
					break;

				default:
					break;
				}
				mTransaction.commit();
			}
		});

	}

	private void hideFragment() {
		if (mHomeFragment != null) {
			mTransaction.hide(mHomeFragment);
		}
		if (mInvestFragment != null) {
			mTransaction.hide(mInvestFragment);
		}
		if (mMeFragment != null) {
			mTransaction.hide(mMeFragment);
		}
		if (mMoreFragment != null) {
			mTransaction.hide(mMoreFragment);
		}
	}

	long mFirstTime=0;
	@Override
	public void onBackPressed() {
		long currentTime = System.currentTimeMillis();
		if (currentTime - mFirstTime > 2000) {
			mFirstTime = currentTime;
			showToast("再按一次退出应用 ！");
		} else {
			ActivityManager.getInstance().removeAll();
		}

	}
}
