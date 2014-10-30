package com.dci.android.gallery.pop;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.dci.android.gallery.PhotoSelectorActivity;
import com.dci.android.gallery.R;
import com.dci.android.gallery.adpter.ImageShowAdapter;
import com.dci.android.gallery.adpter.PhotosAdapter;
import com.dci.android.gallery.adpter.SelectedAdapter.SelectedListener;
import com.dci.android.gallery.model.PhotoModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chanlevel on 14/10/29.
 */
public class ImageShow extends PopupWindow {
	private Context mContext;
	private List<PhotoModel> photos;
	private ViewPager viewPager;
	private View view;
	private LayoutInflater mInflater;
	private ImageShowAdapter adapter;
	private List<View> views;

	public ImageShow(Context context, List<PhotoModel> photos, int position) {
		this.mContext = context;
		this.photos = photos;
		mInflater = LayoutInflater.from(context);
		view = mInflater.inflate(R.layout.pop_imageshow, null);
		// view.setLayoutParams(new
		// WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT));
//		setHeight(LayoutParams.MATCH_PARENT);
//		setWidth(LayoutParams.MATCH_PARENT);
		viewPager = (ViewPager) view.findViewById(R.id.viewpager);

		adapter = new ImageShowAdapter(mContext, photos);
	 
		viewPager.setAdapter(adapter);
		viewPager.setCurrentItem(position);
		setContentView(view);
		setOutsideTouchable(true);
 
		view.setFocusableInTouchMode(true);
		view.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					dismiss();
				}
				return false;
			}
		});
	}

	private void setupList(int position) {
		views = new ArrayList<>();

		View container = null;

		PhotoModel model = photos.get(position);

		if (container == null) {
			container = (View) mInflater.inflate(R.layout.item_image, null);

			((ImageView) container.findViewById(R.id.photo))
					.setImageResource(R.drawable.ic_launcher);

			View view = new View(mContext);
			TextView tView = new TextView(mContext);
			tView.setBackgroundResource(R.drawable.ic_launcher);
			tView.setText(position + "");

			views.add(tView);
		}
	}

}
