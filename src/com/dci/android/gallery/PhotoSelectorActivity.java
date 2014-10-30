package com.dci.android.gallery;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.TextView;

import com.dci.android.gallery.adpter.FragmentAdapter;
import com.dci.android.gallery.fragment.PhotosFragment;
import com.dci.android.gallery.fragment.SelectedFragment;
import com.dci.android.gallery.model.PhotoModel;
import com.dci.android.gallery.util.BitmapCache;
import com.dci.android.gallery.widget.UnderlinePageIndicator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PhotoSelectorActivity extends FragmentActivity {
	public Map<String, List<PhotoModel>> dataList;
	public List<PhotoModel> selectedPhotos;
	public static BitmapCache bitmapCache;
	private Context mContext;
	private DButil db;
	private UnderlinePageIndicator mIndicator;
	private ViewPager mPager;
	private TextView selected, photos;
	private PhotosFragment photosFragment;
	private SelectedFragment selectedFragment;
	private FragmentAdapter mAdapter;
	private int maxSelected = 5;// 默认设置最多选择5张

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		this.mContext = this;
		analysisIntent(getIntent());
		bitmapCache = new BitmapCache();
		new Thread(new Runnable() {
			@Override
			public void run() {
				getDataList();
			}
		}).start();
		initPagers();

	}

	/* 解析intent */
	private void analysisIntent(Intent intent) {
		if (intent == null)
			return;
		maxSelected = intent.getIntExtra("MAXSELECTED", 5);

	}

	public Map<String, List<PhotoModel>> getDataList() {
		if (dataList == null) {
			db = new DButil(mContext);
			dataList = db.getImages();

		}
		return dataList;

	}

	public List<PhotoModel> getSelectedPhotos() {
		if (selectedPhotos == null)
			selectedPhotos = new ArrayList<PhotoModel>();

		return selectedPhotos;
	}

	public TextView getSelected() {
		return selected;
	}

	public int getMaxSelected() {

		return maxSelected;
	}

	public void setMaxSelected(int maxSelected) {
		this.maxSelected = maxSelected;
	}
/*初始化viewpager和indicator*/
	private void initPagers() {
		selected = (TextView) findViewById(R.id.tab_selected);
		photos = (TextView) findViewById(R.id.tab_photos);

		photos.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mIndicator.setCurrentItem(0);

			}
		});
		selected.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mIndicator.setCurrentItem(1);
			}
		});

		List<Fragment> listFragments = new ArrayList<Fragment>();
		photosFragment = PhotosFragment.newInstance();
		selectedFragment = SelectedFragment.newInstance();

		listFragments.add(photosFragment);
		listFragments.add(selectedFragment);

		mAdapter = new FragmentAdapter(getSupportFragmentManager(),
				listFragments);

		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);
		mIndicator = (UnderlinePageIndicator) findViewById(R.id.indicator);
		mIndicator.setSelectedColor(getResources().getColor(
				R.color.indicator_color));
		mIndicator.setFades(false);
		mIndicator.setViewPager(mPager);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_ok) {
			Intent intent = new Intent();
			intent.putStringArrayListExtra("PHOTOS", getResult());
			setResult(RESULT_OK, intent);
			finish();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private ArrayList<String> getResult() {
		ArrayList<String> list = new ArrayList<>();
		for (PhotoModel model : selectedPhotos) {
			list.add(model.getPath());
		}

		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		setResult(RESULT_CANCELED);
		finish();
	}

}
