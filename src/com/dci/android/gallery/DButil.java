package com.dci.android.gallery;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dci.android.gallery.model.PhotoModel;

/**
 * Created by chanlevel on 14/10/24.
 */
public class DButil {
 
	private ContentResolver contentResolver;
	public Map<String, List<PhotoModel>> datalist = new HashMap<String, List<PhotoModel>>();

	public DButil(Context mContext) {
		 
		this.contentResolver = mContext.getContentResolver();
	}

	public Cursor getThumbs() {
		String[] THUM_PROJECTION = { MediaStore.Images.Thumbnails._ID,
				MediaStore.Images.Thumbnails.IMAGE_ID,
				MediaStore.Images.Thumbnails.DATA };
		Cursor c = contentResolver.query(
				MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
				THUM_PROJECTION, null, null, null);
		return c;
	}

	public Map<String, String> getThumbMap() {
		Cursor c = getThumbs();
		Map<String, String> thumbMap = new HashMap<String, String>();
		while (c.moveToNext()) {
			int _id = c.getColumnIndex(MediaStore.Images.Thumbnails.IMAGE_ID);
			int _data = c.getColumnIndex(MediaStore.Images.Thumbnails.DATA);
			String id = c.getString(_id);
			String data = c.getString(_data);
			thumbMap.put(id, data);
		 
		}
		return thumbMap;
	}

	public Map<String, List<PhotoModel>> getImages() {
		Map<String, List<PhotoModel>> datalist = new HashMap<String, List<PhotoModel>>();

		Map<String, String> thumbMap = getThumbMap();
		String[] projection = { Media._ID, Media.TITLE,
				Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.DATA };

		Cursor c = contentResolver.query(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null,
				null, null);
		while (c.moveToNext()) {
			int _id = c.getColumnIndex(Media._ID);
			int _picasaid = c.getColumnIndex(Media.TITLE);
			int _bucketid = c.getColumnIndex(Media.BUCKET_DISPLAY_NAME);
			int _data = c.getColumnIndex(Media.DATA);
			String id = c.getString(_id);
			String title = c.getString(_picasaid);
			String bucketname = c.getString(_bucketid);
			String data = c.getString(_data);
			String thumpath = thumbMap.get(id);
 		 
			if (!datalist.containsKey(bucketname)) {
				datalist.put(bucketname, new ArrayList<PhotoModel>());
			}

			List<PhotoModel> map = datalist.get(bucketname);
			PhotoModel model = new PhotoModel(title, data, thumpath);
			System.out.println(model.toString());
			map.add(model);

		}

		return datalist;

	}
	
	
	
	
	
	
}
