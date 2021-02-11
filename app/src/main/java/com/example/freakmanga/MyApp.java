package com.example.freakmanga;

import android.app.Application;

import java.util.HashMap;
import java.util.Map;

public class MyApp extends Application {
    public static Map<String, String> cookiesz = new HashMap<String, String>() {
        @Override
        public String get(Object key) {
            if (!containsKey(key))
                return "";
            return super.get(key);
        }
    };
    public static String mMenu = "";
    public static final String ua = "Mozilla/5.0 (Linux; Android 6.0.1; SM-G920V Build/MMB29K) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.98 Mobile Safari/537.36";

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
