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
public class PhotosAdapter extends BaseAdapter implements
        AbsListView.OnScrollListener {

    private Context mContext;
    private Map<String, List<PhotoModel>> dataList;
    private List<PhotoModel> currentList;
    private List<PhotoModel> allList;
    private SelectedListener listener;
    private PhotoSelectorActivity activity;
    private int maxSelected;
    private Set<BitmapLoadTask> taskCollection;
    private GridView gridView;

    public PhotosAdapter(Context mContext,
                         Map<String, List<PhotoModel>> dataList, AbsListView listView) {
        this.mContext = mContext;
        this.dataList = dataList;
        listView.setOnScrollListener(this);
        this.gridView = (GridView) listView;
        taskCollection = new HashSet<BitmapLoadTask>();
        setBucket("all");
        activity = (PhotoSelectorActivity) mContext;
        maxSelected = activity.getMaxSelected();
    }

    public void setSelectedListener(SelectedListener listener) {
        this.listener = listener;

    }

    public void setAbsListView(AbsListView listView) {
        listView.setOnScrollListener(this);
        this.gridView = (GridView) listView;
        taskCollection = new HashSet<BitmapLoadTask>();

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
        final ImageView mImageView = holder.imageView;

        mImageView.setTag(model);

        final CheckBox tip = (CheckBox) holder.tip;
        tip.setTag(model);
        tip.setChecked(model.isSelected());
        tip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (tip.getTag() != model)// 添加tag防止checkbox混乱
                    return;

                if (isChecked
                        && maxSelected < activity.getSelectedPhotos().size() + 1) {
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
                if (listener != null)
                    listener.select(isChecked, model);
            }
        });

        return convertView;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // 滑动时停止加载
        if (SCROLL_STATE_FLING == scrollState) {
            for (BitmapLoadTask t : taskCollection) {
                t.cancel(false);
            }
        } else if (scrollState == SCROLL_STATE_IDLE) {
            loadBitmaps(firstVibleItem, visibleItemCount);

        }

    }

    private int firstVibleItem, visibleItemCount;

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        this.firstVibleItem = firstVisibleItem;
        this.visibleItemCount = visibleItemCount;
        // if (firstVisibleItem == 0)//第一次进入直接加载图片
        loadBitmaps(firstVisibleItem, visibleItemCount);

    }

    class ViewHolder {
        CheckBox tip;
        ImageView imageView;

    }

    private void loadBitmaps(int firstVisibleItem, int visibleItemCount) {
        try {
            for (int i = firstVisibleItem; i < firstVisibleItem
                    + visibleItemCount; i++) {

                showBitmap(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showBitmap(int i) {
        PhotoModel model = currentList.get(i);
        ImageView imageView = (ImageView) gridView.findViewWithTag(model);

        Bitmap bitmap = PhotoSelectorActivity.bitmapCache.get(model.getThum_path());
        if (bitmap != null) {

            imageView.setImageBitmap(bitmap);

        } else {
            imageView.setImageResource(R.drawable.ic_stat_upload);
            BitmapLoadTask task = new BitmapLoadTask();
            task.execute(model);
            taskCollection.add(task);

        }
    }

    /*加载本地图片*/
    class BitmapLoadTask extends AsyncTask<PhotoModel, Void, Bitmap> {

        /**
         * 图片的URL地址
         */

        private PhotoModel model;

        @Override
        protected Bitmap doInBackground(PhotoModel... params) {

            // 在后台开始载图片
            Bitmap bitmap = null;
            model = (PhotoModel) params[0];
            if (model != null)
                bitmap = model.getThum(100);
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            ImageView imageView = (ImageView) gridView.findViewWithTag(model);
            if (imageView != null && bitmap != null) {
                Drawable layers[] = new Drawable[]{imageView.getDrawable(),
                        new BitmapDrawable(bitmap)

                };

                TransitionDrawable transitionDrawable = new TransitionDrawable(
                        layers);
                transitionDrawable.setCrossFadeEnabled(true);

                imageView.setImageDrawable(transitionDrawable);
                transitionDrawable.startTransition(400);
                // imageView.setImageBitmap(bitmap);
            }
            taskCollection.remove(this);
        }

    }

    /*设置当前专辑*/
    public void setBucket(String bucket) {
        if (currentList == null)
            currentList = new ArrayList<PhotoModel>();
        if (bucket.equals("all")) {
            if (allList == null) {
                allList = new ArrayList<PhotoModel>();
                Iterator iterator = dataList.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, List<PhotoModel>> map = (Map.Entry<String, List<PhotoModel>>) iterator
                            .next();
                    allList.addAll(map.getValue());

                }
            }
            currentList.clear();
            currentList.addAll(allList);
        } else {
            currentList = dataList.get(bucket);

        }

        this.notifyDataSetChanged();
    }

    public List<PhotoModel> getCurrentList() {
		return currentList;
	}

	 

	public interface SelectedListener {
        public void select(boolean isChecked, PhotoModel photoModel);
    }
}
