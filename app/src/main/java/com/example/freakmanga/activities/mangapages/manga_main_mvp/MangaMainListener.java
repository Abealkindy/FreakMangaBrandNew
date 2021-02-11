package com.example.freakmanga.activities.mangapages.manga_main_mvp;

import com.example.freakmanga.models.MangaMainPageModel;

import java.util.List;

public interface MangaMainListener {
    void onGetDataSuccess(List<MangaMainPageModel> mangaMainPageModel);

    void onGetDataError();
}
