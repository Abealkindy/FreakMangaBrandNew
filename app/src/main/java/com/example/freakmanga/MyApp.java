package com.example.freakmanga;

import android.app.Application;

import androidx.room.Room;

import com.example.freakmanga.data.room.LocalAppDB;

import java.util.HashMap;
import java.util.Map;

public class MyApp extends Application {
    public static LocalAppDB localAppDB;
    public static Map<String, String> cookiesz = new HashMap<String, String>() {
        @Override
        public String get(Object key) {
            if (!containsKey(key))
                return "";
            return super.get(key);
        }
    };
    public static String mMenu = "";
    public static final String ua = "Mozilla/5.0 (Linux; Android 11; SM-A205U; SM-A102U; SM-G960U; SM-N960U; LM-Q720; LM-X420; LM-Q710(FGN)) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Mobile Safari/537.36";

    @Override
    public void onCreate() {
        super.onCreate();
        localAppDB = Room.databaseBuilder(
                getApplicationContext(),
                LocalAppDB.class,
                "local_bookmark_db"
        )
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
    }
}
