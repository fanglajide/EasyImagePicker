package com.dci.android.gallery.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.ImageView;
import com.dci.android.gallery.PhotoSelectorActivity;
import com.dci.android.gallery.util.BitmapCache;
import com.dci.android.gallery.util.Utils;

import java.io.FileNotFoundException;

import java.util.logging.LogRecord;

/**
 * Created by chanlevel on 14/10/24.
 */
public class PhotoModel {

	public String id;
	public String path;
	public String thum_path;
	private boolean isSelected;
	BitmapCache cache;

	public PhotoModel(String id, String path, String thum_path) {
		super();
		this.id = id;
		this.path = path;
		this.thum_path = thum_path;
		this.cache = PhotoSelectorActivity.bitmapCache;
		this.isSelected = false;
	}

	public PhotoModel() {
		cache = PhotoSelectorActivity.bitmapCache;

	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public String getPath() {
		return path;
	}

	public String getThum_path() {
		if (thum_path != null && !thum_path.equals("")
				&& !thum_path.equals("null"))
			return thum_path;
		else
			return path;

	}

	@Override
	public String toString() {
		return "PhotoModel [id=" + id + ", path=" + path + ", thum_path="
				+ thum_path + "]";
	}

	public void insertCache(String path, Bitmap bitmap) {
		if (bitmap != null && cache.get(path) == null)
			cache.put(path, bitmap);

	}

	public Bitmap getbitmap() {

		Bitmap bitmap = cache.get(path);
		if (bitmap == null) {

			try {
				bitmap = Utils.decodeImage(path, 400);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (bitmap != null)
				insertCache(path, bitmap);
		}

		return bitmap;
	}

	public Bitmap getThum(int size) {
		Bitmap bitmap = null;
		if (thum_path != null) {
			bitmap = cache.get(thum_path);
		}
		if (bitmap == null) {
			try {
				bitmap = Utils.decodeImage(path, size);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			thum_path = path + "thum";
			insertCache(thum_path, bitmap);

		}
		return bitmap;
	}

	public void setImageThum(final ImageView imageView) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				final Bitmap bitmap = getThum(100);
				new Thread(new Runnable() {
					@Override
					public void run() {
						Looper.prepare();
						imageView.setImageBitmap(bitmap);
						Looper.loop();
					}
				}).start();

			}
		}).start();

	}

}