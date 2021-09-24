package com.example.freakmanga.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.freakmanga.fragments.NhenFavouriteFragment;
import com.example.freakmanga.fragments.NhenHistoryFragment;

import java.util.Objects;

/*
 * Created by Rosinante24 on 2019-05-30.
 */
public class ViewPagerMangaMenuTabAdapter extends FragmentStatePagerAdapter {
    public ViewPagerMangaMenuTabAdapter(FragmentManager supportFragmentManager) {
        super(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0) {
            fragment = new NhenHistoryFragment();
        } else if (position == 1) {
            fragment = new NhenFavouriteFragment();
        }
        return Objects.requireNonNull(fragment);
    }

    @Override
    public int getCount() {
        return 2;
    }
}
