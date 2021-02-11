package com.example.freakmanga.activities.mangapages.manga_main_mvp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.freakmanga.R;
import com.example.freakmanga.activities.MainActivity;
import com.example.freakmanga.activities.mangapages.read_manga_mvp.ReadMangaActivity;
import com.example.freakmanga.adapters.MainMangaAdapter;
import com.example.freakmanga.databinding.ActivityNhentaiMainBinding;
import com.example.freakmanga.models.MangaMainPageModel;
import com.example.freakmanga.utils.Const;
import com.example.freakmanga.utils.EndlessRecyclerViewScrollListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MangaMainActivity extends AppCompatActivity implements MangaMainListener, SearchView.OnQueryTextListener {
    private ActivityNhentaiMainBinding mBinding;
    private MangaMainPresenter mainPresenter = new MangaMainPresenter(this);
    private String menu = "", hitStatus = "", hitType = "mainPage", textQuery = "";
    private List<MangaMainPageModel> mainPageModelList = new ArrayList<>();
    private MainMangaAdapter newReleasesAdapter;
    private ProgressDialog progressDialog;
    int pageCount = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityNhentaiMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        initUI();
        initEvent();
    }

    private void initEvent() {
        mBinding.swipeRefresh.setOnRefreshListener(() -> {
            hitStatus = "swipeRefresh";
            hitType = "mainPage";
            mBinding.swipeRefresh.setRefreshing(false);
            pageCount = 1;
            getHenData(pageCount++);
        });
    }

    private void initUI() {
        menu = getIntent().getStringExtra("menu");
        isntError(false);
        if (menu != null && !menu.isEmpty()) {
            hitStatus = "newPage";
            hitType = "mainPage";
            initProgressDialog();
            getHenData(pageCount++);
            initRecycler();
        } else {
            isntError(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.searchBar);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);
        return true;
    }

    private void initRecycler() {
        mBinding.recylerHen.setHasFixedSize(true);
        newReleasesAdapter = new MainMangaAdapter(this, mainPageModelList, menu);
        mBinding.recylerHen.setAdapter(newReleasesAdapter);
        GridLayoutManager gridLayoutManager = (GridLayoutManager) mBinding.recylerHen.getLayoutManager();
        mBinding.recylerHen.addOnScrollListener(new EndlessRecyclerViewScrollListener(Objects.requireNonNull(gridLayoutManager)) {
            @Override
            public void onLoadMore(int index, int totalItemsCount, RecyclerView view) {
                hitStatus = "newPage";
                getHenData(pageCount++);
            }
        });
    }

    private void getHenData(int pageCount) {
        String mainURL;
        Log.e("hitType", hitType);
        Log.e("pageCountGlobal", String.valueOf(this.pageCount));
        Log.e("pageCountLocal", String.valueOf(pageCount));
        if (hitType.equalsIgnoreCase("mainPage")) {
            progressDialog.show();
            if (menu.equalsIgnoreCase(getString(R.string.nhentai_tag))) {
                mainURL = Const.BASE_NHEN_URL + String.format(Const.BASE_NHEN_PAGE_URL, pageCount);
            } else if (menu.equalsIgnoreCase(getString(R.string.hennexus_tag))) {
                mainURL = Const.BASE_HENNEXUS_URL + String.format(Const.BASE_GLOBAL_PAGE_URL, pageCount);
            } else {
                mainURL = Const.BASE_HENCAFE_URL + String.format(Const.BASE_GLOBAL_PAGE_URL, pageCount);
            }
            new MyTask(mainURL, menu, hitStatus, this).execute();
        } else {
            if (menu.equalsIgnoreCase(getString(R.string.nhentai_tag))) {
                if (textQuery.matches("[0-9]+")) {
                    Intent intent = new Intent(this, ReadMangaActivity.class);
                    mainURL = Const.BASE_NHEN_URL + "/g/" + textQuery;
                    intent.putExtra("mangaURL", mainURL);
                    intent.putExtra("menu", menu);
                    startActivity(intent);
                } else {
                    progressDialog.show();
                    mainURL = Const.BASE_NHEN_URL + String.format(Const.BASE_NHEN__SEARCH_PAGE_URL, textQuery, pageCount);
                    new MyTask(mainURL, menu, hitStatus, this).execute();
                }
            } else if (menu.equalsIgnoreCase(getString(R.string.hennexus_tag))) {
                progressDialog.show();
                mainURL = Const.BASE_HENNEXUS_URL + String.format(Const.BASE_GLOBAL_PAGE_URL, pageCount) + String.format(Const.BASE_HENNEXUS_SEARCH_URL, textQuery);
                new MyTask(mainURL, menu, hitStatus, this).execute();
            } else {
                progressDialog.show();
                mainURL = Const.BASE_HENCAFE_URL + String.format(Const.BASE_GLOBAL_PAGE_URL, pageCount) + String.format(Const.BASE_HENCAFE_SEARCH_URL, textQuery);
                new MyTask(mainURL, menu, hitStatus, this).execute();
            }
        }
        Log.e("url", mainURL);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (menu != null && !menu.isEmpty() && query != null && !query.isEmpty()) {
            textQuery = query;
            hitStatus = "swipeRefresh";
            hitType = "search";
            pageCount = 1;
            getHenData(pageCount++);
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @SuppressLint("StaticFieldLeak")
    private class MyTask extends AsyncTask<Void, Void, Void> {
        String url, menu, hitStatus;
        Context context;

        public MyTask(String url, String menu, String hitStatus, Context context) {
            this.url = url;
            this.menu = menu;
            this.hitStatus = hitStatus;
            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mainPresenter.getMangaData(this.url, this.menu, this.hitStatus, this.context);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

        }
    }

    private void initProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Be patient please onii-chan, it just take less than a minute :3");
    }

    @Override
    public void onGetDataSuccess(List<MangaMainPageModel> mangaMainPageModel) {
        runOnUiThread(() -> {
            isntError(false);
            if (hitStatus.equalsIgnoreCase("newPage")) {
                progressDialog.dismiss();
                if (mangaMainPageModel != null && !mangaMainPageModel.isEmpty()) {
                    mainPageModelList.addAll(mangaMainPageModel);
                    newReleasesAdapter.notifyDataSetChanged();
                } else {
                    if (mainPageModelList != null && !mainPageModelList.isEmpty()) {
                        Toast.makeText(this, "There's no more page!", Toast.LENGTH_LONG).show();
                    } else {
                        isntError(true);
                    }
                }
            } else if (hitStatus.equalsIgnoreCase("swipeRefresh")) {
                progressDialog.dismiss();
                if (mainPageModelList != null) {
                    mainPageModelList.clear();
                    mainPageModelList.addAll(mangaMainPageModel);
                }
                newReleasesAdapter.notifyDataSetChanged();
            }
            Log.e("list", new Gson().toJson(mainPageModelList));
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(MangaMainActivity.this, MainActivity.class));
        finish();
    }

    private void isntError(boolean isError) {
        if (isError) {
            mBinding.recylerHen.setVisibility(View.GONE);
            Glide.with(this).asGif().load(R.raw.aquacry).into(mBinding.imageError);
            mBinding.linearError.setVisibility(View.VISIBLE);
        } else {
            mBinding.recylerHen.setVisibility(View.VISIBLE);
            mBinding.linearError.setVisibility(View.GONE);
        }
    }

    @Override
    public void onGetDataError() {
        runOnUiThread(() -> {
            progressDialog.dismiss();
            if (mainPageModelList != null && !mainPageModelList.isEmpty()) {
                Toast.makeText(this, "There's no more page!", Toast.LENGTH_LONG).show();
            } else {
                isntError(true);
            }
        });
    }
}