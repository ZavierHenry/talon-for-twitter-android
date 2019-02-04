package com.klinker.android.twitter_l.data.roomdb.daos

import android.database.Cursor
import androidx.room.*

import com.klinker.android.twitter_l.data.roomdb.entities.Hashtag

@Dao
abstract class HashtagDao {


    fun createHashtag(tag: String) : Hashtag? {
        return insertHashtag(Hashtag(tag = tag))
    }

    private fun insertHashtag(hashtag: Hashtag) : Hashtag? {
        return insertTag(hashtag)?.let { id -> hashtag.copy(id = id) }
    }


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    internal abstract fun insertTag(hashtag: Hashtag) : Long?

    @Delete
    abstract fun deleteTag(hashtag: Hashtag)

    //getCursor implementation
    @Query("SELECT * FROM hashtags WHERE name LIKE '%' || :tag || '%'")
    abstract fun getHashtags(tag: String): List<Hashtag>


//    @Query("SELECT * FROM hashtags WHERE name LIKE '%' || :tag || '%' LIMIT :pageSize OFFSET ((:page - 1) * :pageSize)")
//    fun getHashtags(tag: String, page: Int = 1, pageSize : Int = 200)

    //trimDatabase implementation
    @Query("DELETE FROM hashtags WHERE id <= (SELECT id FROM hashtags ORDER BY id DESC LIMIT 1 OFFSET :trimSize)")
    abstract fun trimDatabase(trimSize: Int)

}
