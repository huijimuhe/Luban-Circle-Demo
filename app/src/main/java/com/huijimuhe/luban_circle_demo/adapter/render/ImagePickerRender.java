package com.huijimuhe.luban_circle_demo.adapter.render;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.huijimuhe.luban_circle_demo.R;
import com.huijimuhe.luban_circle_demo.adapter.base.AbstractRender;
import com.huijimuhe.luban_circle_demo.adapter.base.AbstractRenderAdapter;
import com.huijimuhe.luban_circle_demo.adapter.base.AbstractViewHolder;
import com.huijimuhe.luban_circle_demo.utils.ViewUtils;

/**
 * Copyright (C) 2016 Huijimuhe Technologies. All rights reserved.
 * Contact: 20903213@qq.com Zengweizhou
 */
public class ImagePickerRender extends AbstractRender {
    private ViewHolder mHolder;
    private AbstractRenderAdapter mAdapter;

    public ImagePickerRender(ViewGroup parent, AbstractRenderAdapter adapter) {
        this.mAdapter = adapter;
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listitem_image_picker, parent, false);
        this.mHolder = new ViewHolder(v, adapter);
    }


    @Override
    public void bindData(int position) {
        String data = (String) mAdapter.getItem(position);
        mHolder.mImage.setImageURI(Uri.parse(data));
    }

    @Override
    public AbstractViewHolder getReusableComponent() {
        return this.mHolder;
    }

    public class ViewHolder extends AbstractViewHolder {
        public ImageView mImage;

        public ViewHolder(View v, final AbstractRenderAdapter adapter) {
            super(v);
            mImage = ViewUtils.findViewById(v, R.id.iv_image);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapter.mOnItemClickListener.onItemClick(view, getLayoutPosition());
                }
            });
        }
    }
}
