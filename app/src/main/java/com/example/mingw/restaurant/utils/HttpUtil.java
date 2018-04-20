package com.example.mingw.restaurant.utils;

import java.io.IOException;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class HttpUtil {

    /**
     * 使用OkHttp发送get请求
     * @param address
     * @return
     */
    public static String getStringByOkHttp(String address){
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                .get()
                .url(address)
                .build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                ResponseBody responseBody = response.body();
                assert responseBody != null;
                return responseBody.string();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 使用OKHttp post订单数据
     * @param address url
     * @param formBody form
     * @return
     */
    public static String postFormByOkHttp(String address, FormBody formBody) {
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                .url(address)
                .post(formBody)
                .build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                ResponseBody responseBody = response.body();
                assert responseBody != null;
                String r = responseBody.string();
                return r;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
