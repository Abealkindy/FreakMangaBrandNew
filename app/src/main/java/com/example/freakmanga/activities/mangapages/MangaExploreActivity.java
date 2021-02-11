package com.example.freakmanga.activities.mangapages;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import com.example.freakmanga.databinding.ActivityMangaExploreBinding;

public class MangaExploreActivity extends AppCompatActivity {
    ActivityMangaExploreBinding mBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMangaExploreBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
    }
}