package com.example.freakmanga.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;


import com.example.freakmanga.R;
import com.example.freakmanga.activities.mangapages.manga_main_mvp.MangaMainActivity;
import com.example.freakmanga.databinding.ActivityMainBinding;
import com.example.freakmanga.networks.InternetConnection;
import com.zhkrb.cloudflare_scrape_webview.CfCallback;
import com.zhkrb.cloudflare_scrape_webview.Cloudflare;
import com.zhkrb.cloudflare_scrape_webview.util.ConvertUtil;

import java.net.HttpCookie;
import java.util.List;

import static com.example.freakmanga.MyApp.mMenu;
import static com.example.freakmanga.MyApp.ua;
import static com.example.freakmanga.MyApp.cookiesz;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
        mainBinding.hencafeMenuText.setOnClickListener(v -> connectionCheck("https://hentai.cafe/", getString(R.string.hencafe_tag)));
        mainBinding.hennexusMenuText.setOnClickListener(v -> connectionCheck("https://hentainexus.com/", getString(R.string.hennexus_tag)));
        mainBinding.nhentaiMenuText.setOnClickListener(v -> connectionCheck("https://nhentai.net/", getString(R.string.nhentai_tag)));
    }

    private void connectionCheck(String url, String menu) {
        if (InternetConnection.checkConnection(this)) {
            try {
                Cloudflare cloudflare = new Cloudflare(this, url);
                cloudflare.setUser_agent(ua);
                cloudflare.setCfCallback(new CfCallback() {
                    @Override
                    public void onSuccess(List<HttpCookie> cookieList, boolean hasNewUrl, String newUrl) {
                        cookiesz = ConvertUtil.List2Map(cookieList);
                        mMenu = menu;
                        Toast.makeText(MainActivity.this, "Berhasil!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, MangaMainActivity.class);
                        intent.putExtra("menu", menu);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        cloudflare.cancel();
                        Toast.makeText(MainActivity.this, "Lagi tutup dulu, nanti balik lagi yah!", Toast.LENGTH_LONG).show();
                    }
                });
                cloudflare.getCookies();
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "Lagi tutup dulu, nanti balik lagi yah!", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(MainActivity.this, "Cek internetnya dulu boss, nanti balik lagi yah!", Toast.LENGTH_LONG).show();
        }
    }
}