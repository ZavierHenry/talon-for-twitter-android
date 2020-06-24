package com.klinker.android.twitter_l.data.roomdb

import androidx.room.*


@Entity(tableName = "hashtags", indices = [Index(value = ["tag"], unique = true)])
data class Hashtag(val tag: String, @PrimaryKey(autoGenerate = true) val id: Long? = null)

@Dao
abstract class HashtagDao : BaseDao<Hashtag>() {

    @Query("SELECT * FROM hashtags WHERE tag LIKE :pattern ORDER BY id ASC LIMIT :pageSize OFFSET ((:page - 1) * :pageSize)")
    protected abstract fun queryPattern(pattern: String, page: Int = 1, pageSize: Int = 200) : List<Hashtag>

    @Query("DELETE FROM hashtags WHERE id NOT IN (SELECT id FROM hashtags ORDER BY id DESC LIMIT :size)")
    abstract fun trimDatabase(size: Int)

    fun getHashtags(tag: String, page: Int = 1, pageSize: Int = 200) : List<Hashtag> {
        return queryPattern("%$tag%", page, pageSize)
    }
}