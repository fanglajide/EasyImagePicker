package com.dci.android.gallery.fragment;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.dci.android.gallery.PhotoSelectorActivity;
import com.dci.android.gallery.R;

/**
 * Created by chanlevel on 14/10/24.
 */
public abstract class BaseFragment extends Fragment {
	protected LayoutInflater mInflater;
	protected Context mContext;
	protected View v;
	protected GridView gridView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext = getActivity();
		mInflater = LayoutInflater.from(getActivity());

		setupViews();

		return v;
	}

	protected abstract void setupViews();

	protected int getHeight() {
		return ((PhotoSelectorActivity) mContext).getWindow().getDecorView().getHeight()
				- getTop();
	}

	protected int getWidth() {

		return ((PhotoSelectorActivity) mContext).getWindow().getDecorView().getWidth();
	}

	private int getTop() {
		Rect frame = new Rect();
		((PhotoSelectorActivity) mContext).getWindow().getDecorView()
				.getWindowVisibleDisplayFrame(frame);
		return frame.top;
	}

}
