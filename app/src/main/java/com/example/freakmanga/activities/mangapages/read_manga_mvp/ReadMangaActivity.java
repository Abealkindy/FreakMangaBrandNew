package com.example.freakmanga.activities.mangapages.read_manga_mvp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.freakmanga.R;
import com.example.freakmanga.adapters.ReadMangaAdapter;
import com.example.freakmanga.databinding.ActivityReadMangaBinding;

import java.util.ArrayList;
import java.util.List;

public class ReadMangaActivity extends AppCompatActivity implements ReadMangaListener {
    private ActivityReadMangaBinding mBinding;
    private ReadMangaPresenter readMangaPresenter = new ReadMangaPresenter(this);
    private List<String> imageContentListLocal = new ArrayList<>();
    private ProgressDialog progressDialog;
    private String menu = "", url = "";

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
        initProgressDialog();
        getContentData();
    }

    private void initProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Be patient please onii-chan, it just take less than a minute :3");
    }

    private void getContentData() {
        progressDialog.show();
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

    @Override
    public void onGetImageContentSuccess(List<String> imageContentList, String title) {
        runOnUiThread(() -> {
            progressDialog.dismiss();
            mBinding.mangaTitleText.setText(title);
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