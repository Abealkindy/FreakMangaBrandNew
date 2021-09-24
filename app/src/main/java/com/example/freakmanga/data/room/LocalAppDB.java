package com.example.freakmanga.data.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.freakmanga.data.room.manhwahen_local.bookmark.ManhwahenBookmarkDAO;
import com.example.freakmanga.data.room.manhwahen_local.bookmark.ManhwahenBookmarkTable;
import com.example.freakmanga.data.room.manhwahen_local.history.ManhwahenHistoryDAO;
import com.example.freakmanga.data.room.manhwahen_local.history.ManhwahenHistoryTable;
import com.example.freakmanga.data.room.nhen_local.bookmark.NhenBookmarkDAO;
import com.example.freakmanga.data.room.nhen_local.bookmark.NhenBookmarkTable;
import com.example.freakmanga.data.room.nhen_local.history.NhenHistoryDAO;
import com.example.freakmanga.data.room.nhen_local.history.NhenHistoryTable;

@Database(
        entities = {
                ManhwahenBookmarkTable.class,
                ManhwahenHistoryTable.class,
                NhenBookmarkTable.class,
                NhenHistoryTable.class
        },
        version = 2
)
public abstract class LocalAppDB extends RoomDatabase {
    public abstract ManhwahenBookmarkDAO manhwahenBookmarkDAO();

    public abstract ManhwahenHistoryDAO manhwahenHistoryDAO();

    public abstract NhenBookmarkDAO nhenBookmarkDAO();

    public abstract NhenHistoryDAO nhenHistoryDAO();
}
