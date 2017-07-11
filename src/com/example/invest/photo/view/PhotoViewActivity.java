package com.example.invest.photo.view;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.invest.R;

public class PhotoViewActivity extends Activity {
	private LinearLayout mRescoPhotoContainer;
	private TextView mInstructionsPhotoNumberTv;
	/****************************************/
	private ArrayList<String> mImageUrls;
	public static final String IMAGE_URL_LIST_KEY = "IMAGE_URL_LIST_KEY";
	/****************************************/
	private int mCurrentPoint = 0;
	public static final String CURRENT_POINT_KEY = "CURRENT_POINT_KEY";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_view);
		// 鎵惧埌鎺т欢
		mRescoPhotoContainer = (LinearLayout) findViewById(R.id.resco_photo);
		// 鑾峰彇鍙傛暟
		mCurrentPoint = getIntent().getIntExtra(CURRENT_POINT_KEY,
				mCurrentPoint);// 褰撳墠鐐瑰嚮鐨勪綅缃�
		mImageUrls = getIntent().getStringArrayListExtra(IMAGE_URL_LIST_KEY);// 鎬荤殑鍥剧墖閾炬帴ArrayList

		mInstructionsPhotoNumberTv = (TextView) findViewById(R.id.instructions_photo_number);

		// 璁剧疆鍐呭
		PhotoViewPager pager = new PhotoViewPager(this,
				mInstructionsPhotoNumberTv);
		pager.setUriList(mImageUrls);
		pager.setCurrentItem(mCurrentPoint);
		mRescoPhotoContainer.removeAllViews();
		mRescoPhotoContainer.addView(pager);
	}
}
