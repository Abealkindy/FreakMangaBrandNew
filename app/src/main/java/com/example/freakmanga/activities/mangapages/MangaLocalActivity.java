package com.example.freakmanga.activities.mangapages;

import static com.example.freakmanga.MyApp.localAppDB;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.widget.Toast;

import com.example.freakmanga.R;
import com.example.freakmanga.adapters.ViewPagerMangaMenuTabAdapter;
import com.example.freakmanga.databinding.ActivityMangaExploreBinding;
import com.google.android.material.tabs.TabLayout;

public class MangaLocalActivity extends AppCompatActivity {
    ActivityMangaExploreBinding mBinding;
    boolean isSecondBack = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMangaExploreBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        UISettings();
        addUIEvents();
    }

    private void addUIEvents() {
        mBinding.tabHome.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mBinding.viewPagerTabs.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 0) {
                    setTitle("History");
                } else if (tab.getPosition() == 1) {
                    setTitle("Favourite");
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void UISettings() {
        setTitle("History");
        mBinding.tabHome.addTab(mBinding.tabHome.newTab().setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_baseline_history_24, getTheme())));
        mBinding.tabHome.addTab(mBinding.tabHome.newTab().setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_favorite_filled_24dp, getTheme())));
        mBinding.tabHome.setTabIconTint(ColorStateList.valueOf(getResources().getColor(android.R.color.white)));
        mBinding.viewPagerTabs.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mBinding.tabHome));
        mBinding.viewPagerTabs.setAdapter(new ViewPagerMangaMenuTabAdapter(getSupportFragmentManager()));
    }


    @Override
    public void onBackPressed() {
        int tabPos = mBinding.viewPagerTabs.getCurrentItem();
        if (tabPos == 0) {
            if (!isSecondBack) {
                isSecondBack = true;
                Toast.makeText(this, "Klik/swipe back sekali lagi untuk kembali ke halaman utama", Toast.LENGTH_LONG).show();
            } else {
                isSecondBack = false;
                finish();
            }
        } else if (tabPos == 1) {
            mBinding.viewPagerTabs.setCurrentItem(0);
        }

    }
}