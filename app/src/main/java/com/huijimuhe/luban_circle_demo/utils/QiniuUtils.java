package com.huijimuhe.luban_circle_demo.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.huijimuhe.luban_circle_demo.network.BaseClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONObject;

import java.io.File;

import cz.msebera.android.httpclient.Header;

/**
 * Copyright (C) 2016 Huijimuhe Technologies. All rights reserved.
 * Contact: 20903213@qq.com Zengweizhou
 */
public class QiniuUtils {
    private static final String TAG = QiniuUtils.class.getName();

    private AsyncExecutor executor;

    private QiniuUtils(Context context) {
        executor = new AsyncExecutor();

    }

    public static QiniuUtils from(Context context) {
        return new QiniuUtils(context);
    }

    public void queue(final File file, final UploadListener callback) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                BaseClient.getToken(null, new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        callback.onError(1);
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        UploadWorker worker = new UploadWorker(file, responseString, callback);
                        executor.execute(worker);
                    }
                });
            }
        });

    }


    private class UploadWorker extends AsyncExecutor.Worker<String> {

        private File file;
        private UploadListener callback;
        private String token;
        private UploadManager uploadManager = new UploadManager();

        public UploadWorker(File file, String token, UploadListener l) {
            this.file = file;
            this.token = token;
            this.callback = l;
        }

        @Override
        protected String doInBackground() {
            uploadManager.put(file, null, token,
                    new UpCompletionHandler() {
                        @Override
                        public void complete(String key, ResponseInfo info, JSONObject response) {
                            try {
                                if (info.statusCode == 200) {
                                    // 上传图片返回key值
                                    callback.onSuccess(file, JsonUtils.getString("key", response));
                                } else {
                                    callback.onError(info.statusCode);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                callback.onError(1);
                            }
                        }
                    }, null);
            return null;
        }

    }

    public interface UploadListener {
        void onSuccess(File compressedFile, String key);

        void onError(int code); //TODO 定义自己的错误码
    }
}
