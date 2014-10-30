package com.dci.android.gallery.adpter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.AsyncTask;
import android.view.*;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;

import com.dci.android.gallery.PhotoSelectorActivity;
import com.dci.android.gallery.R;
import com.dci.android.gallery.model.PhotoModel;

import java.util.*;

/**
 * Created by chanlevel on 14/10/24.
 */
public class SelectedAdapter extends BaseAdapter   {
	private Context mContext;

	private List<PhotoModel> currentList;

	private SelectedListener listener;

 
 

	public SelectedAdapter(Context mContext, List<PhotoModel> dataList) {
		this.mContext = mContext;
		this.currentList = dataList;

	}

	public void setSelectedListener(SelectedListener listener) {
		this.listener = listener;

	}

 

	@Override
	public int getCount() {
		return currentList.size();
	}

	@Override
	public Object getItem(int position) {
		return currentList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView,
			final ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {

			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_image, null);
			holder = new ViewHolder();
			holder.tip = (CheckBox) convertView.findViewById(R.id.selectTip);
			holder.imageView = (ImageView) convertView.findViewById(R.id.photo);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final PhotoModel model = currentList.get(position);
		final ImageView mImageView = (ImageView) holder.imageView;

		mImageView.setTag(model);
		mImageView.setImageBitmap(currentList.get(position).getThum(100));
		final CheckBox tip = (CheckBox) holder.tip;
		tip.setTag(model);
		tip.setChecked(model.isSelected());
		tip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {

				if (tip.getTag() != model)
					return;
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
				if (listener != null)
					listener.select(isChecked, model);
			}
		});

		return convertView;
	}

	class ViewHolder {
		CheckBox tip;
		ImageView imageView;

	}

	public interface SelectedListener {
		public void select(boolean isChecked, PhotoModel photoModel);
	}
}
