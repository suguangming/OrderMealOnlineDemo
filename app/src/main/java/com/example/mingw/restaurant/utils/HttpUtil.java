package com.example.mingw.restaurant.utils;

import android.util.Log;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class HttpUtil {

    public static void sendOkHttpRequest(String address, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
            .url(address)
            .build();
        client.newCall(request).enqueue(callback);
    }

    public static String getStringByOkhttp(String address){
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                .get()
                .url(address)
                .build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                ResponseBody responseBody = response.body();
                return responseBody.string();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

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
                String r = responseBody.string();
                return r;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
