package com.dci.android.gallery.util;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * Created by chanlevel on 14/10/24.
 */
public class BitmapCache extends LruCache<String,Bitmap> {
   // private final int maxSize= (int) (Runtime.getRuntime().maxMemory()/8);
    public BitmapCache( ) {
        super((int) (Runtime.getRuntime().maxMemory()/8));

    }

    @Override
    protected int sizeOf(String key, Bitmap value) {
        return value.getRowBytes()*value.getHeight();
    }


}
