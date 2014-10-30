package com.dci.android.gallery.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.Toast;
import com.dci.android.gallery.model.PhotoModel;

/**
 * Created by chanlevel on 14/10/24.
 */
public class SelectTip extends ImageView implements Checkable {
    private boolean isChecked = false;
    private TipListener listener;
    private PhotoModel model;

    public interface TipListener {
        public void select(boolean isChecked );
    }

    public void setListener(TipListener listener) {
        this.listener = listener;
    }


    public SelectTip(Context context, boolean isChecked) {
        super(context);
        this.isChecked = isChecked;
    }

    public SelectTip(Context context, AttributeSet attrs, boolean isChecked) {
        super(context, attrs,0);
        this.isChecked = isChecked;
    }

    public SelectTip(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setChecked(boolean checked) {
        isChecked = !isChecked;

        if (listener != null ) {
            listener.select(isChecked );


        } else
            System.out.print("seletTip no listener");


    }

    @Override
    public boolean isChecked() {

        return isChecked;
    }

    @Override
    public void toggle() {
        setChecked(!isChecked);
    }
}
