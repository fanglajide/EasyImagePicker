package com.dci.android.gallery.fragment;

import java.util.List;

import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.PopupWindow.OnDismissListener;

import com.dci.android.gallery.PhotoSelectorActivity;
import com.dci.android.gallery.R;
import com.dci.android.gallery.adpter.SelectedAdapter;
import com.dci.android.gallery.adpter.SelectedAdapter.SelectedListener;
import com.dci.android.gallery.model.PhotoModel;
import com.dci.android.gallery.pop.ImageShow;

/**
 * Created by chanlevel on 14/10/24.
 */
public class SelectedFragment extends BaseFragment {
	static SelectedFragment fragment;
	private SelectedAdapter adapter;
	public List<PhotoModel> selectedPhotos;

	public static SelectedFragment newInstance() {
		if (fragment == null)
			fragment = new SelectedFragment();
		return fragment;
	}

	@Override
	protected void setupViews() {
		v = mInflater.inflate(R.layout.fragment_selected, null);
		gridView = (GridView) v.findViewById(R.id.gridview);
		gridView.setOnItemClickListener(new GridViewListener());
		initgridView();
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
			ImageShow pop = new ImageShow(mContext, selectedPhotos, position);
			pop.setFocusable(true);
			pop.setWidth(getWidth());
			pop.setHeight(getHeight());
			pop.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss() {
					// TODO Auto-generated method stub
					freshData();
				}
			});
			pop.showAtLocation(view, Gravity.BOTTOM, 0, 0);
		}
	}

	/* 数据刷新 */
	private void freshData() {
		for (PhotoModel model : selectedPhotos) {
			if (!model.isSelected())
				selectedPhotos.remove(model);
		}
		adapter.notifyDataSetChanged();

	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		// TODO Auto-generated method stub
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser && adapter != null) {
			freshData();
			// adapter.notifyDataSetChanged();

		}
	}

	private void initgridView() {
		selectedPhotos = ((PhotoSelectorActivity) mContext).getSelectedPhotos();
		adapter = new SelectedAdapter(mContext, selectedPhotos);
		gridView.setAdapter(adapter);
		adapter.setSelectedListener(new SelectedListener() {

			@Override
			public void select(boolean isChecked, PhotoModel photoModel) {
				// TODO Auto-generated method stub
				if (selectedPhotos == null)
					selectedPhotos = ((PhotoSelectorActivity) mContext)
							.getSelectedPhotos();
				if (isChecked) {
					selectedPhotos.add(photoModel);
				} else
					selectedPhotos.remove(photoModel);

				((PhotoSelectorActivity) mContext).getSelected().setText(
						"已选择" + selectedPhotos.size() + "张");
				adapter.notifyDataSetChanged();
			}
		});
	}

}
