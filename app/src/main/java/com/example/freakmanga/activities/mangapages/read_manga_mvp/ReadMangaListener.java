package com.example.freakmanga.activities.mangapages.read_manga_mvp;

import java.util.List;

public interface ReadMangaListener {
    void onGetImageContentSuccess(List<String> imageContentList, String title);

    void onGetImageContentError();
}
