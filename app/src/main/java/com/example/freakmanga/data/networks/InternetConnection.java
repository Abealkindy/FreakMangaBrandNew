package com.example.freakmanga.data.networks;

import android.content.Context;
import android.net.ConnectivityManager;

import androidx.annotation.NonNull;

import java.util.Objects;

import okhttp3.Dns;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.dnsoverhttps.DnsOverHttps;


public class InternetConnection {

    /**
     * CHECK WHETHER INTERNET CONNECTION IS AVAILABLE OR NOT
     */
    public static boolean checkConnection(@NonNull Context context) {
        return ((ConnectivityManager) Objects.requireNonNull(context.getSystemService
                (Context.CONNECTIVITY_SERVICE))).getActiveNetworkInfo() != null;
    }

    /**
     * DoH with okhttp to access source url
     */
    public static OkHttpClient dnsClient() {
        OkHttpClient bootstrapClient = new OkHttpClient();
        Dns privateDns = new DnsOverHttps.Builder().client(bootstrapClient)
                .url(HttpUrl.get("https://dns.adguard-dns.com/dns-query"))
                .build();
        return new OkHttpClient.Builder().dns(privateDns).build();
    }
}
