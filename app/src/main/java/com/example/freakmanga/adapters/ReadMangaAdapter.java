package com.example.freakmanga.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicConvolve3x3;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.freakmanga.MyApp.mMenu;

import com.example.freakmanga.R;
import com.example.freakmanga.data.networks.InternetConnection;
import com.example.freakmanga.databinding.ReadMangaItemListBinding;
import com.example.freakmanga.utils.ConvolutionMatrix;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;
import java.util.Objects;

import javax.xml.XMLConstants;

import okhttp3.Dns;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.dnsoverhttps.DnsOverHttps;

public class ReadMangaAdapter extends RecyclerView.Adapter<ReadMangaAdapter.ViewHolder> {
    private Context context;
    private List<String> henModelList;

    public ReadMangaAdapter(Context context, List<String> henModelList) {
        this.context = context;
        this.henModelList = henModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        ReadMangaItemListBinding itemListBinding = ReadMangaItemListBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(itemListBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        loadImage(holder, position);
        holder.itemListBinding.reloadImage.setOnClickListener(v -> {
            reloadVisibleorNot(holder, false);
            loadImage(holder, position);
        });
    }

    private void loadImage(ViewHolder holder, int position) {
        Transformation transformation = new Transformation() {
            @Override
            public Bitmap transform(Bitmap source) {
                Bitmap scaledBitmap;
                if (source.getByteCount() / 1000000 >= 20) {
                    scaledBitmap = Bitmap.createScaledBitmap(
                            source,
                            (int) (source.getWidth() * 0.5),
                            (int) (source.getHeight() * 0.5),
                            true
                    );
                } else {
                    scaledBitmap = source;
                }
                if (scaledBitmap != source) {
                    // Same bitmap is returned if sizes are the same
                    source.recycle();
                }
                return scaledBitmap;
            }

            @Override
            public String key() {
                return "transformation" + " desiredWidth";
            }
        };

        Picasso picasso = new Picasso.Builder(context).downloader(new OkHttp3Downloader(InternetConnection.dnsClient())).build();
        picasso.load(henModelList.get(position))
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE)
                .transform(transformation)
                .placeholder(Objects.requireNonNull(ResourcesCompat.getDrawable(context.getResources(), R.drawable.imageplaceholder, context.getTheme())))
                .error(Objects.requireNonNull(ResourcesCompat.getDrawable(context.getResources(), R.drawable.error, context.getTheme())))
                .into(holder.itemListBinding.imageMangaContentItem, new Callback() {
                    @Override
                    public void onSuccess() {
                        Log.e("Success load?", "yes");
                        reloadVisibleorNot(holder, false);
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e("Success load?", "no : " + e.getMessage());
                        reloadVisibleorNot(holder, true);
                    }
                });
    }

    private void reloadVisibleorNot(ViewHolder holder, boolean isVisible) {
        if (isVisible) {
            holder.itemListBinding.reloadImage.setVisibility(View.VISIBLE);
            holder.itemListBinding.imageMangaContentItem.setVisibility(View.GONE);
        } else {
            holder.itemListBinding.reloadImage.setVisibility(View.GONE);
            holder.itemListBinding.imageMangaContentItem.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return henModelList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ReadMangaItemListBinding itemListBinding;

        public ViewHolder(final ReadMangaItemListBinding itemViewList) {
            super(itemViewList.getRoot());
            this.itemListBinding = itemViewList;
        }
    }

}
