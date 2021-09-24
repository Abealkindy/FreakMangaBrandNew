package com.example.freakmanga.data.room.nhen_local.history;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NhenHistoryDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertHistoryData(NhenHistoryTable... mangaBookmarkModel);

    @Query("SELECT * FROM tb_nhen_history ORDER BY chapter_added_date DESC")
    List<NhenHistoryTable> getMangaHistoryData();

    @Query("SELECT * FROM tb_nhen_history WHERE chapter_title LIKE :mangaTitle ORDER BY chapter_title ASC")
    List<NhenHistoryTable> searchByName(String mangaTitle);
}
