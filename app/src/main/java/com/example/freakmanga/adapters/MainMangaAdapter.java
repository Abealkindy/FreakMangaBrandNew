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
import com.example.freakmanga.databinding.MangaItemListBinding;
import com.example.freakmanga.models.MangaMainPageModel;
import com.example.freakmanga.utils.Const;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Objects;

public class MainMangaAdapter extends RecyclerView.Adapter<MainMangaAdapter.ViewHolder> {
    private Context context;
    private List<MangaMainPageModel> henModelList;
    private String menu;

    public MainMangaAdapter(Context context, List<MangaMainPageModel> henModelList, String menu) {
        this.context = context;
        this.henModelList = henModelList;
        this.menu = menu;
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
        holder.itemListBinding.textTitle.setText(henModelList.get(position).getMangaTitle());
        try {
            Glide.with(context)
                    .asDrawable()
                    .load(new URL(henModelList.get(position).getThumbURL()))
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
                mangaURL = Const.BASE_NHEN_URL + henModelList.get(position).getChapterURL();
            } else if (menu.equalsIgnoreCase(context.getString(R.string.hennexus_tag))) {
                mangaURL = Const.BASE_HENNEXUS_URL + henModelList.get(position).getChapterURL();
            } else {
                mangaURL = henModelList.get(position).getChapterURL();
            }
            Intent intent = new Intent(context.getApplicationContext(), ReadMangaActivity.class);
            intent.putExtra("mangaURL", mangaURL);
            intent.putExtra("menu", menu);
            intent.putExtra("mangaThumb", henModelList.get(position).getThumbURL());
            intent.putExtra("mangaTitle", henModelList.get(position).getMangaTitle());
            context.startActivity(intent);
            Log.e("menu", menu);
        });
    }

    @Override
    public int getItemCount() {
        return henModelList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private MangaItemListBinding itemListBinding;

        public ViewHolder(final MangaItemListBinding itemViewList) {
            super(itemViewList.getRoot());
            this.itemListBinding = itemViewList;
        }
    }

}
