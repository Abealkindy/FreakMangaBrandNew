package com.example.freakmanga.data.networks;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.util.Objects;
import okhttp3.Request;
import okhttp3.Response;

public class JsoupConfig {
    public static Document setInitJsoup(String url) {
        try {
            Response result =
                    InternetConnection.dnsClient().newCall(new Request.Builder().url(url).build())
                            .execute();
            return setHtmlParseJsoup(result.body().string());
        } catch (Exception e) {
            Log.e("jsoupError", Objects.requireNonNull(e.getMessage()));
            return null;
        }
    }

    public static Document setHtmlParseJsoup(String htmlPage) {
        return Jsoup.parse(htmlPage);
    }
}
