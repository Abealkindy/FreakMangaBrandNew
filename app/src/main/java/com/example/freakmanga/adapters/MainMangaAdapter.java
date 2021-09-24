package com.example.freakmanga.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.freakmanga.R;
import com.example.freakmanga.activities.mangapages.read_manga_mvp.ReadMangaActivity;
import com.example.freakmanga.data.room.nhen_local.bookmark.NhenBookmarkTable;
import com.example.freakmanga.data.room.nhen_local.history.NhenHistoryTable;
import com.example.freakmanga.databinding.MangaItemListBinding;
import com.example.freakmanga.fragments.NhenHistoryFragment;
import com.example.freakmanga.models.MangaMainPageModel;
import com.example.freakmanga.utils.Const;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Objects;

public class MainMangaAdapter extends RecyclerView.Adapter<MainMangaAdapter.ViewHolder> {
    private Context context;
    private List<MangaMainPageModel> henModelList;
    private List<NhenHistoryTable> henHistoryList;
    private List<NhenBookmarkTable> henBookmarkList;
    private String menu = "";
    private boolean isFromMainList = false;

    public MainMangaAdapter(Context context, List<MangaMainPageModel> henModelList, String menu) {
        this.context = context;
        this.henModelList = henModelList;
        this.menu = menu;
        this.isFromMainList = true;
    }

    public MainMangaAdapter(Context context, List<NhenHistoryTable> henHistoryList) {
        this.context = context;
        this.henHistoryList = henHistoryList;
        this.menu = context.getString(R.string.nhentai_tag);
        this.isFromMainList = false;
    }

    public MainMangaAdapter(Context context, List<NhenBookmarkTable> henBookmarkList, boolean bookmark) {
        this.context = context;
        this.henBookmarkList = henBookmarkList;
        this.menu = context.getString(R.string.nhentai_tag);
        this.isFromMainList = false;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        MangaItemListBinding itemListBinding = MangaItemListBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(itemListBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String title, thumb, url;
        if (henModelList != null) {
            title = henModelList.get(position).getMangaTitle();
            thumb = henModelList.get(position).getThumbURL();
            url = henModelList.get(position).getChapterURL();
        } else if (henHistoryList != null) {
            title = henHistoryList.get(position).getChapterTitle();
            thumb = henHistoryList.get(position).getChapterThumb();
            url = henHistoryList.get(position).getChapterURL();
        } else {
            title = henBookmarkList.get(position).getMangaTitle();
            thumb = henBookmarkList.get(position).getMangaThumb();
            url = henBookmarkList.get(position).getMangaDetailURL();
        }
        holder.itemListBinding.textTitle.setText(title);
        try {
            Glide.with(context)
                    .asDrawable()
                    .load(new URL(thumb))
                    .apply(
                            new RequestOptions()
                                    .transform(new RoundedCorners(20))
                                    .timeout(30000)
                    )
                    .error(Objects.requireNonNull(ResourcesCompat.getDrawable(context.getResources(), R.drawable.error, context.getTheme())))
                    .placeholder(Objects.requireNonNull(ResourcesCompat.getDrawable(context.getResources(), R.drawable.imageplaceholder, context.getTheme())))
                    .into(holder.itemListBinding.imageThumb);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        holder.itemListBinding.relativeItem.setOnClickListener(v -> {
            String mangaURL;
            if (menu.equalsIgnoreCase(context.getString(R.string.nhentai_tag))) {
                if (isFromMainList) {
                    mangaURL = Const.BASE_NHEN_URL + url;
                } else {
                    mangaURL = url;
                }
            } else if (menu.equalsIgnoreCase(context.getString(R.string.hennexus_tag))) {
                mangaURL = Const.BASE_HENNEXUS_URL + url;
            } else {
                mangaURL = url;
            }
            Intent intent = new Intent(context.getApplicationContext(), ReadMangaActivity.class);
            intent.putExtra("mangaURL", mangaURL);
            intent.putExtra("menu", menu);
            intent.putExtra("mangaThumb", thumb);
            intent.putExtra("mangaTitle", title);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        if (henModelList != null) {
            return henModelList.size();
        } else if (henHistoryList != null) {
            return henHistoryList.size();
        } else {
            return henBookmarkList.size();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private MangaItemListBinding itemListBinding;

        public ViewHolder(final MangaItemListBinding itemViewList) {
            super(itemViewList.getRoot());
            this.itemListBinding = itemViewList;
        }
    }

}
