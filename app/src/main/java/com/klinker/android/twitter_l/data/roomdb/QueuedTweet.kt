package com.klinker.android.twitter_l.data.roomdb

import androidx.room.*

@Entity(tableName = "queued_tweets")
data class QueuedTweet @JvmOverloads constructor(
        val text: String,
        val account: Int,
        @PrimaryKey(autoGenerate = true) val id: Long = 0
)

@Dao
abstract class QueuedTweetDao : BaseDao<QueuedTweet>() {

    fun insert(entity: QueuedTweet): QueuedTweet {
        val id = insertEntity(entity)
        return entity.copy(id = id)
    }

    fun insert(entities: List<QueuedTweet>): List<QueuedTweet> {
        return insertEntities(entities).mapIndexed { index, id ->
            entities[index].copy(id = id)
        }
    }

    fun insert(vararg entities: QueuedTweet): List<QueuedTweet> {
        return insertEntities(*entities).mapIndexed { index, id ->
            entities[index].copy(id = id)
        }
    }

    @Query("SELECT * FROM queued_tweets WHERE account = :account LIMIT :pageSize OFFSET ((:page - 1) * :pageSize)")
    abstract fun getQueuedTweets(account: Int, page: Int = 1, pageSize: Int = 200) : List<QueuedTweet>

}