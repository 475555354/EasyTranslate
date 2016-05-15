package com.example.kid.easytranslate.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by niuwa on 2016/5/15.
 */
public class HttpUtil {
    public static void sendHttpRequest(final String address,final HttpCallBackListener httpCallBackListener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try{
                    Log.d("123456", "13254");
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null){
                        response.append(line).append("\n");
                    }
                    Log.d("123456", response.toString());
                    if (httpCallBackListener != null)
                        httpCallBackListener.onFinish(response.toString());
                }catch (Exception e){
                    if (httpCallBackListener != null)
                        httpCallBackListener.onError(e);
                }finally {
                    if (connection != null)
                        connection.disconnect();
                }
            }
        }).start();
    }
}
