package com.klinker.android.twitter_l.data.roomdb.daos;

import android.database.Cursor;

import com.klinker.android.twitter_l.data.roomdb.entities.Hashtag;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface HashtagDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertTag(Hashtag hashtag);

    //getCursor implementation
    @Query("SELECT * FROM hashtags WHERE name LIKE '%' || :tag || '%'")
    Cursor getHashtagCursor(String tag);

    //trimDatabase implementation
    @Query("DELETE FROM hashtags WHERE id NOT IN (SELECT id FROM hashtags ORDER BY id DESC LIMIT :trimSize)")
    void trimDatabase(int trimSize);

}
