package com.dci.android.gallery.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Rect;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.animation.ScaleAnimation;
import android.widget.*;
import android.widget.PopupWindow.OnDismissListener;

import com.dci.android.gallery.PhotoSelectorActivity;
import com.dci.android.gallery.R;
import com.dci.android.gallery.adpter.PhotosAdapter;
import com.dci.android.gallery.model.PhotoModel;
import com.dci.android.gallery.pop.ImageShow;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by chanlevel on 14/10/24.
 */
public class PhotosFragment extends BaseFragment {
	static PhotosFragment fragment;
	Map<String, List<PhotoModel>> dataList;
	private PhotosAdapter adapter;
	private Button bucket;
	private Dialog dialog;
	private List<PhotoModel> selectedPhotos;

	public static PhotosFragment newInstance() {
		if (fragment == null)
			fragment = new PhotosFragment();
		return fragment;
	}

	@Override
	protected void setupViews() {
		v = mInflater.inflate(R.layout.fragment_photo, null);
		gridView = (GridView) v.findViewById(R.id.gridview);
		dataList = ((PhotoSelectorActivity) mContext).getDataList();
		selectedPhotos = ((PhotoSelectorActivity) mContext).getSelectedPhotos();
		bucket = (Button) v.findViewById(R.id.bucket);
		bucket.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setupDialog();
			}
		});
		adapter = new PhotosAdapter(mContext, dataList, gridView);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new GridViewListener());
		adapter.setSelectedListener(new PhotosAdapter.SelectedListener() {
			@Override
			public void select(boolean isChecked, PhotoModel photoModel) {
				if (selectedPhotos == null)
					selectedPhotos = ((PhotoSelectorActivity) mContext)
							.getSelectedPhotos();
				if (isChecked) {
					selectedPhotos.add(photoModel);
				} else
					selectedPhotos.remove(photoModel);

				((PhotoSelectorActivity) mContext).getSelected().setText(
						"已选择" + selectedPhotos.size() + "张");

			}
		});
	}

	class GridViewListener implements AdapterView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			 
			int left = view.getLeft();
			int right = view.getRight();
			int top = view.getTop();
			int bottom = view.getBottom();
			int width = view.getWidth();
			int height = view.getHeight();
			int parentHeight = parent.getRootView().getMeasuredHeight();
			int parentWidth = parent.getRootView().getMeasuredWidth();
			float fromx = (float) width / (float) parentWidth;
			float fromy = (float) height / (float) parentHeight;
			ScaleAnimation animation = new ScaleAnimation(fromx, 1f, fromy, 1f);
			ImageShow pop = new ImageShow(mContext, adapter.getCurrentList() , position);
			pop.setFocusable(true);
			pop.setWidth(getWidth());
			pop.setHeight(getHeight());
			pop.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss() {
					// TODO Auto-generated method stub
					setUserVisibleHint(true);
				}
			});
			pop.showAtLocation(view, Gravity.BOTTOM, 0, 0);
		}
	}

	private List<String> getKey() {
		List<String> keyList = new ArrayList<String>();
		keyList.add("全部图片");
		Iterator iterator = dataList.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, List<PhotoModel>> map = (Map.Entry<String, List<PhotoModel>>) iterator
					.next();
			keyList.add(map.getKey());
		}

		return keyList;
	}

	private void setupDialog() {
		dialog = new Dialog(mContext);
		dialog.setTitle("选择相册");
		ListView keyView = new ListView(mContext);
		final List<String> keylist = getKey();
		keyView.setAdapter(new ArrayAdapter<String>(mContext,
				android.R.layout.simple_expandable_list_item_1, keylist));

		keyView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == 0)
					adapter.setBucket("all");
				else
					adapter.setBucket(keylist.get(position));
				dialog.dismiss();
			}
		});

		dialog.setContentView(keyView);
		dialog.show();

	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		// TODO Auto-generated method stub
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser && adapter != null)
			adapter.notifyDataSetChanged();
	}

}
