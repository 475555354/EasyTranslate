package com.example.kid.easytranslate.util;

/**
 * Created by niuwa on 2016/5/15.
 */
public interface HttpCallBackListener {
    void onFinish(String response);
    void onError(Exception e);
}
