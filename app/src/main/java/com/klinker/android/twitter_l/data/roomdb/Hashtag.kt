package com.klinker.android.twitter_l.data.roomdb

import androidx.room.*

@Entity(tableName = "hashtags", indices = [Index(value = ["tag"], unique = true)])
data class Hashtag @JvmOverloads constructor(
        val tag: String,
        @PrimaryKey(autoGenerate = true) val id: Long = 0
) : BaseDao.TalonEntity<Hashtag> {

    override fun copyWithId(id: Long): Hashtag {
        return this.copy(id = id)
    }
}

@Dao
abstract class HashtagDao : BaseDao<Hashtag>() {

    @Query("SELECT * FROM hashtags WHERE tag LIKE :pattern")
    protected abstract fun queryPattern(pattern: String) : List<Hashtag>

    @Query("DELETE FROM hashtags WHERE id NOT IN (SELECT id FROM hashtags ORDER BY id DESC LIMIT :size)")
    abstract fun trimDatabase(size: Int)

    fun getHashtags(tag: String) : List<Hashtag> {
        return queryPattern("%$tag%")
    }
}