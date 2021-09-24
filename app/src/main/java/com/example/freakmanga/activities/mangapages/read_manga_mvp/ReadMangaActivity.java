package com.example.freakmanga.activities.mangapages.read_manga_mvp;

import static com.example.freakmanga.MyApp.localAppDB;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.freakmanga.R;
import com.example.freakmanga.adapters.ReadMangaAdapter;
import com.example.freakmanga.data.room.nhen_local.bookmark.NhenBookmarkTable;
import com.example.freakmanga.data.room.nhen_local.history.NhenHistoryTable;
import com.example.freakmanga.databinding.ActivityReadMangaBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ReadMangaActivity extends AppCompatActivity implements ReadMangaListener {
    private ActivityReadMangaBinding mBinding;
    private ReadMangaPresenter readMangaPresenter = new ReadMangaPresenter(this);
    private List<String> imageContentListLocal = new ArrayList<>();
    private ProgressDialog progressDialog;
    private String menu = "", url = "", thumbURL = "", title = "";
    private boolean isFavourite = false;
    NhenBookmarkTable mangaBookmarkModel = new NhenBookmarkTable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityReadMangaBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        initUI();
        initEvent();
    }

    private void initEvent() {
        mBinding.buttonReload.setOnClickListener(v -> getContentData());
        mBinding.buttonFavourite.setOnClickListener(view -> {
            if (!isFavourite) {
                Date dateNow = Calendar.getInstance().getTime();
                @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
                String formattedDate = df.format(dateNow);
                mangaBookmarkModel.setMangaAddedDate(formattedDate);
                mangaBookmarkModel.setMangaTitle(title);
                mangaBookmarkModel.setMangaThumb(thumbURL);
                mangaBookmarkModel.setMangaDetailURL(url);
                localAppDB.nhenBookmarkDAO().insertBookmarkData(mangaBookmarkModel);
                setFavourite(true);
            } else {
                localAppDB.nhenBookmarkDAO().deleteBookmarkItem(url);
                setFavourite(false);
            }
        });
        mBinding.buttonShare.setOnClickListener(view -> {
            Intent shareToOther = new Intent(Intent.ACTION_SEND);
            shareToOther.setType("text/plain");
            shareToOther.putExtra(Intent.EXTRA_SUBJECT, "Share " + title + " ke teman-teman mu\n");
            shareToOther.putExtra(Intent.EXTRA_TEXT, "Share " + title + " ke teman-teman mu\n" + url);
            startActivity(Intent.createChooser(shareToOther, "Share URL"));
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    private void initUI() {
        menu = getIntent().getStringExtra("menu");
        url = getIntent().getStringExtra("mangaURL");
        thumbURL = getIntent().getStringExtra("mangaThumb");
        title = getIntent().getStringExtra("mangaTitle");
        insertToHistoryTable();
        checkBookmarkData();
        initProgressDialog();
        getContentData();
    }

    private void insertToHistoryTable() {
        Date dateNow = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String formattedDate = df.format(dateNow);
        NhenHistoryTable mangaBookmarkModel = new NhenHistoryTable();
        mangaBookmarkModel.setChapterAddedDate(formattedDate);
        mangaBookmarkModel.setChapterTitle(title);
        mangaBookmarkModel.setChapterURL(url);
        mangaBookmarkModel.setChapterThumb(thumbURL);
        localAppDB.nhenHistoryDAO().insertHistoryData(mangaBookmarkModel);
    }

    private void checkBookmarkData() {
        NhenBookmarkTable nhenBookmarkTable = localAppDB.nhenBookmarkDAO().findByURL(url);
        setFavourite(nhenBookmarkTable != null && !nhenBookmarkTable.getMangaDetailURL().isEmpty() && nhenBookmarkTable.getMangaDetailURL().equals(url));
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setFavourite(boolean isFavourite) {
        this.isFavourite = isFavourite;
        if (isFavourite) {
            mBinding.buttonFavourite.setImageDrawable(getDrawable(R.drawable.ic_favorite_filled_24dp));
        } else {
            mBinding.buttonFavourite.setImageDrawable(getDrawable(R.drawable.ic_favorite_unfilled_24dp));
        }
    }

    private void initProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Be patient please onii-chan, it just take less than a minute :3");
    }

    private void getContentData() {
        runOnUiThread(() -> progressDialog.show());
        new MyTask(url, menu, this).execute();
    }

    @SuppressLint("StaticFieldLeak")
    private class MyTask extends AsyncTask<Void, Void, Void> {
        String url, menu;
        Context context;

        public MyTask(String url, String menu, Context context) {
            this.url = url;
            this.menu = menu;
            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            readMangaPresenter.getHenImageContent(this.url, this.menu, this.context);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onGetImageContentSuccess(List<String> imageContentList, String title) {
        runOnUiThread(() -> {
            progressDialog.dismiss();
            mBinding.mangaTitleText.setText(title + "\n" + url.substring(url.indexOf("g/") + 2, url.length() - 2));
            if (imageContentList != null && !imageContentList.isEmpty()) {
                isntError(false);
                if (imageContentListLocal != null) {
                    imageContentListLocal.clear();
                }
                imageContentListLocal = imageContentList;
                mBinding.recyclerReadManga.setAdapter(new ReadMangaAdapter(ReadMangaActivity.this, imageContentListLocal));
            } else {
                isntError(true);
            }

        });
    }

    private void isntError(boolean isError) {
        if (isError) {
            mBinding.recyclerReadManga.setVisibility(View.GONE);
            Glide.with(this).asGif().load(R.raw.aquacry).into(mBinding.imageError);
            mBinding.linearError.setVisibility(View.VISIBLE);
        } else {
            mBinding.recyclerReadManga.setVisibility(View.VISIBLE);
            mBinding.linearError.setVisibility(View.GONE);
        }
    }


    @Override
    public void onGetImageContentError() {
        runOnUiThread(() -> {
            progressDialog.dismiss();
            isntError(true);
        });
    }
}