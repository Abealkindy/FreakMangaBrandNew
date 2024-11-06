package com.example.freakmanga.activities.mangapages.read_manga_mvp;

import static com.example.freakmanga.MyApp.localAppDB;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.example.freakmanga.R;
import com.example.freakmanga.data.networks.JsoupConfig;
import com.example.freakmanga.data.room.nhen_local.bookmark.NhenBookmarkTable;
import com.google.gson.Gson;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ReadMangaPresenter {
    ReadMangaListener readMangaListener;

    public ReadMangaPresenter(ReadMangaListener readMangaListener) {
        this.readMangaListener = readMangaListener;
    }

    public void getHenImageContent(String url, String menu, Context context) {
        Document document = JsoupConfig.setInitJsoup(url);
        if (document != null) {
            List<String> henModelList = new ArrayList<>();
            Elements elements;
            String title;
            if (menu.equalsIgnoreCase(context.getString(R.string.nhentai_tag))) {
                elements = document.getElementsByClass("gallerythumb");
                String test, tests = "";
                title = document.getElementsByTag("h1").get(0).text();
                for (Element el : elements) {
                    String images = el.getElementsByTag("img").attr("data-src");
                    Log.e("imageFromTag", images);
                    if (images.substring(8).startsWith("t")) {
                        test = images.substring(8).replaceFirst("t", "i");
                        if (images.contains("t.png")) {
                            tests = test.replace("t.png", ".png");
                        }
                        if (images.contains("t.jpg")) {
                            tests = test.replace("t.jpg", ".jpg");
                        }
                        if (images.contains("t.gif")) {
                            tests = test.replace("t.gif", ".gif");
                        }
                    }
                    henModelList.add("https://" + tests);
                }
                readMangaListener.onGetImageContentSuccess(henModelList, title);
                /* set favourite image status */
                NhenBookmarkTable nhenBookmarkTable = localAppDB.nhenBookmarkDAO().findByURL(url);
                readMangaListener.onFavouriteChanged(nhenBookmarkTable != null && !nhenBookmarkTable.getMangaDetailURL().isEmpty() && nhenBookmarkTable.getMangaDetailURL().equals(url));
            } else if (menu.equalsIgnoreCase("henNexus")) {
                title = document.getElementsByTag("h1").text();
                elements = document.getElementsByClass("column is-2-fullhd is-one-fifth-widescreen is-one-fifth-desktop is-one-quarter-tablet");
                for (Element element : elements) {
                    String images = element.getElementsByTag("img").attr("src");
                    henModelList.add(images);
                }
                readMangaListener.onGetImageContentSuccess(henModelList, title);
            } else {
                title = document.getElementsByTag("h3").text();
                String readURL = document.getElementsByClass("x-btn x-btn-flat x-btn-rounded x-btn-large").attr("href");
                Document docs = JsoupConfig.setInitJsoup(readURL + "page/1");
                if (docs != null) {
                    String totalPage = docs.getElementsByClass("text").text();
                    String totalPageFinal = totalPage.substring(0, totalPage.length() - 2);
                    for (int position = 1; position <= Integer.parseInt(totalPageFinal); position++) {
                        Document docss = JsoupConfig.setInitJsoup(readURL + "page/" + position);
                        if (docss != null) {
                            String images = docss.getElementsByClass("open").attr("src");
                            henModelList.add(images);
                        }

                    }
                }
                readMangaListener.onGetImageContentSuccess(henModelList, title);
            }
            Log.e("image url result", new Gson().toJson(henModelList));
            Log.e("title", title);
        } else {
            readMangaListener.onGetImageContentError();
        }
    }

    public void setFavourite(String title, String thumbUrl, String contentUrl) {
        NhenBookmarkTable nhenBookmarkTable = localAppDB.nhenBookmarkDAO().findByURL(contentUrl);
        boolean isFavourite = nhenBookmarkTable != null &&
                !nhenBookmarkTable.getMangaDetailURL().isEmpty() &&
                nhenBookmarkTable.getMangaDetailURL().equals(contentUrl);
        if (!isFavourite) {
            Date dateNow = Calendar.getInstance().getTime();
            @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
            String formattedDate = df.format(dateNow);
            NhenBookmarkTable mangaBookmarkModel = new NhenBookmarkTable();
            mangaBookmarkModel.setMangaAddedDate(formattedDate);
            mangaBookmarkModel.setMangaTitle(title);
            mangaBookmarkModel.setMangaThumb(thumbUrl);
            mangaBookmarkModel.setMangaDetailURL(contentUrl);
            localAppDB.nhenBookmarkDAO().insertBookmarkData(mangaBookmarkModel);

            readMangaListener.onFavouriteChanged(true);
        } else {
            localAppDB.nhenBookmarkDAO().deleteBookmarkItem(contentUrl);

            readMangaListener.onFavouriteChanged(false);
        }
    }
}
