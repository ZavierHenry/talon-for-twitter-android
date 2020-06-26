package com.klinker.android.twitter_l.data.roomdb

import androidx.room.*

@Entity(tableName = "hashtags", indices = [Index(value = ["tag"], unique = true)])
data class Hashtag @JvmOverloads constructor(
        val tag: String,
        @PrimaryKey(autoGenerate = true) val id: Long = 0
)

@Dao
abstract class HashtagDao : BaseDao<Hashtag>() {

    fun insert(vararg entities: Hashtag): List<Hashtag> {
        return insertEntities(*entities).mapIndexed { index, id ->
            entities[index].copy(id = id)
        }
    }

    fun insert(entities: List<Hashtag>): List<Hashtag> {
        return insertEntities(entities).mapIndexed { index, id ->
            entities[index].copy(id = id)
        }
    }

    fun insert(entity: Hashtag): Hashtag {
        val id = insertEntity(entity)
        return entity.copy(id = id)
    }

    @Query("SELECT * FROM hashtags WHERE tag LIKE :pattern")
    protected abstract fun queryPattern(pattern: String) : List<Hashtag>

    @Query("DELETE FROM hashtags WHERE id NOT IN (SELECT id FROM hashtags ORDER BY id DESC LIMIT :size)")
    abstract fun trimDatabase(size: Int)

    fun getHashtags(tag: String) : List<Hashtag> {
        return queryPattern("%$tag%")
    }
}