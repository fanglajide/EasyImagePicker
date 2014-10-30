package com.dci.android.gallery.adpter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by chanlevel on 14/10/24.
 */
public class FragmentAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> listFragments;

public FragmentAdapter(FragmentManager manager,List<Fragment > listFragments){
    super(manager);
    this.listFragments=listFragments;


}


    @Override
    public int getCount() {
        return 2;
    }




    @Override
    public Fragment getItem(int i) {
        return listFragments.get(i);
    }


}
