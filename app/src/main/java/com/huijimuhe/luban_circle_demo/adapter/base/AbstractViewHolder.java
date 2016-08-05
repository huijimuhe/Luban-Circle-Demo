package com.huijimuhe.luban_circle_demo.adapter.base;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;

public abstract class AbstractViewHolder extends RecyclerView.ViewHolder{

    private static final String TAG = AbstractViewHolder.class.getSimpleName();
    private SparseArray<View> holder = null;
    public AbstractViewHolder(View itemView) {
        super(itemView);
    }

    /**
     * 获取一个缓存的view
     *
     * @param id
     * @param <T>
     * @return
     */
    public <T extends View> T obtainView(int id) {
        if (null == holder) {
            holder = new SparseArray<>();
        }
        View view = holder.get(id);
        if (null != view) {
            return (T) view;
        }
        view = itemView.findViewById(id);
        if (null == view) {
            Log.e(TAG, "no view that id is " + id);
            return null;
        }
        holder.put(id, view);
        return (T) view;
    }

    /**
     * 获取一个缓存的view，并自动转型
     *
     * @param id
     * @param <T>
     * @return
     */
    public <T> T obtainView(int id, Class<T> viewClazz) {
        View view = obtainView(id);
        if (null == view) {
            return null;
        }
        return (T) view;
    }
}
