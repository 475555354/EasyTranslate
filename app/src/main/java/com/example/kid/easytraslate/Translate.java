package com.example.kid.easytraslate;

import android.util.Log;

import com.example.kid.easytraslate.util.MD5;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Random;

/**
 * Created by niuwa on 2016/4/7.
 */
public class Translate {

    public static String translation;

    public static String translate(String q,String from, String to){


        int salt = new Random().nextInt(10000);
        final String appid = "20160401000017405";
        final String token = "SIiXZhlNCHMi8lVRB3sm";

        final String string = appid + q + salt + token;
        String md5 = MD5.GetMD5Code(string, true);
        Log.d("MD5:", md5);

        final String address = "http://api.fanyi.baidu.com/api/trans/vip/translate?q=" + q
                + "&from=" + from + "&to=" + to + "&appid=" + appid + "&salt=" + salt
                + "&sign=" + md5;

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try{
                    URL url = new URL(address);
                    connection = (HttpURLConnection)url.openConnection();
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

                    JSONObject jsonObject = new JSONObject(response.toString());
                    JSONArray array = (JSONArray)jsonObject.get("trans_result");
                    JSONObject dst = (JSONObject) array.get(0);
                    String text = dst.getString("dst");
                    translation = URLDecoder.decode(text,"utf-8");

                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    if(connection != null)
                        connection.disconnect();
                }
            }
        }).start();

        if (translation != null)
            return translation;
        else
            return "";
    }

}
