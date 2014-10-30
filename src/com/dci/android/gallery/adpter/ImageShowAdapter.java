package com.dci.android.gallery.adpter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.dci.android.gallery.PhotoSelectorActivity;
import com.dci.android.gallery.R;
import com.dci.android.gallery.adpter.SelectedAdapter.SelectedListener;
import com.dci.android.gallery.model.PhotoModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chanlevel on 14/10/29.
 */
public class ImageShowAdapter extends PagerAdapter {
	private List<PhotoModel> photos;
	private List<View> views;
	private LayoutInflater mInflater;
	private Context mContext;
	private List<Boolean> isloadList;
	private int maxSelected;
	private PhotoSelectorActivity activity;
	private SelectedListener listener;
	private int currentSelected;

	public ImageShowAdapter(Context mContext, List<PhotoModel> photos) {
		this.mContext = mContext;
		this.photos = photos;
		views = new ArrayList<View>();
		isloadList = new ArrayList<>();
		mInflater = LayoutInflater.from(mContext);
		maxSelected = ((PhotoSelectorActivity) mContext).getMaxSelected();
		activity = (PhotoSelectorActivity) mContext;
		currentSelected = activity.getSelectedPhotos().size();
		for (int i = 0; i < photos.size(); i++) {
			setupList(i);
		}
		loadData(0);
	}

	public void setSelectedListener(SelectedListener listener) {
		this.listener = listener;

	}

	private void setupList(int position) {
		View container = null;

		PhotoModel model = photos.get(position);
		ViewHolder holder = null;
		if (container == null) {
			container = (View) mInflater.inflate(R.layout.item_viewpager, null);
			holder = new ViewHolder();
			holder.imageView = (ImageView) container.findViewById(R.id.photo);
			holder.checkBox = (CheckBox) container.findViewById(R.id.selectTip);
			container.setTag(holder);
		} else
			holder = (ViewHolder) container.getTag();

		// holder.checkBox.setChecked(model.isSelected());
		// holder.imageView.setImageBitmap(model.getbitmap());

		isloadList.add(false);
		views.add(container);
	}

	@Override
	public int getCount() {
		return photos.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object o) {
		return view == o;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		loadData(position + 1);
		loadData(position - 1);
		container.addView(views.get(position));
		return views.get(position);
	}

	/* 预加载 */
	private void loadData(int position) {
		if (position < 0 || position >= photos.size())
			return;
		if (isloadList.get(position))
			return;
		View view = views.get(position);
		final PhotoModel model = photos.get(position);
		final CheckBox tip = ((CheckBox) view.findViewById(R.id.selectTip));
		tip.setChecked(model.isSelected());
		tip.setTag(model);
		((ImageView) view.findViewById(R.id.photo)).setImageBitmap(model
				.getbitmap());

		tip.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (tip.getTag() != model)// 添加tag防止checkbox混乱
					return;
				System.out.println(maxSelected + "-------"
						+ activity.getSelectedPhotos().size());
				if (isChecked && maxSelected < currentSelected + 1) {

					tip.toggle();
					Toast.makeText(mContext, "最多选择" + maxSelected + "张", 200)
							.show();
					return;
				}

				if (isChecked == true) {
					Animation animation = AnimationUtils.loadAnimation(
							mContext, R.anim.photo_selection_added);

					buttonView.startAnimation(animation);
				} else {
					Animation animation = AnimationUtils.loadAnimation(
							mContext, R.anim.photo_selection_removed);

					buttonView.startAnimation(animation);

				}

				model.setSelected(isChecked);
				if (isChecked)
					currentSelected++;
				else
					currentSelected--;

			}
		});

		Boolean b = isloadList.get(position);
		b = true;
	}

	class ViewHolder {
		ImageView imageView;
		CheckBox checkBox;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {

		// super.destroyItem(container, position, object);
		container.removeView(views.get(position));
	}

}
