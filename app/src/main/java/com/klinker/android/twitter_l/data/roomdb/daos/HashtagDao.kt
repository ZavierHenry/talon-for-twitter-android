package com.klinker.android.twitter_l.data.roomdb.daos

import android.database.Cursor

import com.klinker.android.twitter_l.data.roomdb.entities.Hashtag

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface HashtagDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertTag(hashtag: Hashtag)

    //getCursor implementation
    @Query("SELECT * FROM hashtags WHERE name LIKE '%' || :tag || '%'")
    fun getHashtagCursor(tag: String): Cursor

    //trimDatabase implementation
    @Query("DELETE FROM hashtags WHERE id <= (SELECT id FROM hashtags ORDER BY id DESC LIMIT 1 OFFSET :trimSize)")
    fun trimDatabase(trimSize: Int)

}
