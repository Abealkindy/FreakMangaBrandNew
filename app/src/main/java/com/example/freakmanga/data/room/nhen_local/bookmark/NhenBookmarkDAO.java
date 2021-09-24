package com.example.freakmanga.data.room.nhen_local.bookmark;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NhenBookmarkDAO {
    @Query("SELECT * FROM tb_nhen_bookmark ORDER BY series_added_date DESC")
    List<NhenBookmarkTable> getNhenBookmarkData();

    @Query("SELECT * FROM tb_nhen_bookmark WHERE series_title LIKE :seriesTitle ORDER BY series_title ASC")
    List<NhenBookmarkTable> searchByName(String seriesTitle);

    @Query("SELECT * FROM tb_nhen_bookmark WHERE series_url LIKE :mangaURL")
    NhenBookmarkTable findByURL(String mangaURL);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBookmarkData(NhenBookmarkTable... mangaBookmarkModel);

    @Query("DELETE FROM tb_nhen_bookmark WHERE series_url = :seriesURL")
    void deleteBookmarkItem(String seriesURL);
}
