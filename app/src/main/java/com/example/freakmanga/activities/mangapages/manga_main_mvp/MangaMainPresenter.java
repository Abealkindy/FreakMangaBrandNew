package com.example.freakmanga.activities.mangapages.manga_main_mvp;

import android.content.Context;
import android.util.Log;

import com.example.freakmanga.R;
import com.example.freakmanga.models.MangaMainPageModel;
import com.example.freakmanga.data.networks.JsoupConfig;
import com.google.gson.Gson;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class MangaMainPresenter {
    MangaMainListener mainListener;

    public MangaMainPresenter(MangaMainListener mainListener) {
        this.mainListener = mainListener;
    }

    public void getMangaData(String url, String menu, String hitStatus, Context context) {
        Document document;
        document = JsoupConfig.setInitJsoup(url);
        if (document != null) {
            List<MangaMainPageModel> henModelList = new ArrayList<>();
            Elements elements;
            if (menu.equalsIgnoreCase(context.getString(R.string.nhentai_tag))) {
                elements = document.getElementsByClass("gallery");
                for (Element element : elements) {
                    String henUrl = element.getElementsByTag("a").attr("href");
                    String henThumbURL = element.getElementsByTag("img").attr("data-src");
                    String henTitle = element.getElementsByClass("caption").text();

                    MangaMainPageModel henModel = new MangaMainPageModel();
                    henModel.setMangaTitle(henTitle);
                    henModel.setChapterURL(henUrl);
                    henModel.setThumbURL(henThumbURL);
                    henModelList.add(henModel);
                }
            } else if (menu.equalsIgnoreCase(context.getString(R.string.hennexus_tag))) {
                elements = document.getElementsByClass("column is-one-fifth-fullhd is-one-quarter-widescreen is-one-quarter-desktop is-one-third-tablet is-half-mobile");
                for (Element element : elements) {
                    String henUrl = element.getElementsByTag("a").attr("href");
                    String henThumbURL = element.getElementsByTag("img").attr("src");
                    String henTitle = element.getElementsByClass("card-header-title").text();

                    MangaMainPageModel henModel = new MangaMainPageModel();
                    henModel.setMangaTitle(henTitle);
                    henModel.setChapterURL(henUrl);
                    henModel.setThumbURL(henThumbURL);
                    henModelList.add(henModel);
                }
            } else {
                elements = document.getElementsByTag("article");
                for (Element element : elements) {
                    String henUrl = element.getElementsByTag("a").attr("href");
                    String henThumbURL = element.getElementsByTag("img").attr("src");
                    String henTitle = element.getElementsByClass("entry-title").text();

                    MangaMainPageModel henModel = new MangaMainPageModel();
                    henModel.setMangaTitle(henTitle);
                    henModel.setChapterURL(henUrl);
                    henModel.setThumbURL(henThumbURL);
                    henModelList.add(henModel);
                }
            }
            if (!henModelList.isEmpty()) {
                Log.e("result", new Gson().toJson(henModelList));
                mainListener.onGetDataSuccess(henModelList);
            } else {
                if (hitStatus.equalsIgnoreCase("newPage")) {
                    Log.e("result", new Gson().toJson(henModelList));
                    mainListener.onGetDataSuccess(henModelList);
                } else {
                    mainListener.onGetDataError();
                }
            }
        } else {
            mainListener.onGetDataError();
        }
    }
}
