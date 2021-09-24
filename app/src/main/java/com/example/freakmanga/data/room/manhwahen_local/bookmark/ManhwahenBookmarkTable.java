package com.example.freakmanga.data.room.manhwahen_local.bookmark;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Data;

@Data
@Entity(tableName = "tb_manhwahen_bookmark")
public class ManhwahenBookmarkTable {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "series_url")
    String mangaDetailURL = "";
    @ColumnInfo(name = "series_added_date")
    String mangaAddedDate = "";
    @ColumnInfo(name = "series_title")
    String mangaTitle = "";
    @ColumnInfo(name = "series_thumb")
    String mangaThumb = "";
}
