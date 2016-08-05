package com.huijimuhe.luban_circle_demo.network;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

public class BaseClient {

    public static final String URL_GET_OSS = "http://yourdomain/token"; //// TODO: 2016/8/5 替换你的七牛服务器token地址

    private static AsyncHttpClient client = new AsyncHttpClient();

    /**
     * 获取token
     * TODO 修改为你的网络模块
     * @param params
     * @param responseHandler
     */
    public static void getToken(RequestParams params, TextHttpResponseHandler responseHandler) {

        client.get(URL_GET_OSS, params, responseHandler);
    }

}
