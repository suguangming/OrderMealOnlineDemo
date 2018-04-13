package com.example.mingw.restaurant.utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import okhttp3.OkHttpClient;
import okhttp3.Request;
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
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
            .get()
            .url(address)
            .build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                ResponseBody body = response.body();
                return body.string();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
