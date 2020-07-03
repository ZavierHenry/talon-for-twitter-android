package com.klinker.android.twitter_l.data.roomdb

import androidx.room.*

@Entity(tableName = "queued_tweets")
data class QueuedTweet @JvmOverloads constructor(
        val text: String,
        val account: Int,
        @PrimaryKey(autoGenerate = true) val id: Long = 0
) : BaseDao.TalonEntity<QueuedTweet> {

    override fun copyWithId(id: Long): QueuedTweet {
        return this.copy(id = id)
    }
}

@Dao
abstract class QueuedTweetDao : BaseDao<QueuedTweet>() {

    @Query("SELECT * FROM queued_tweets WHERE account = :account LIMIT :pageSize OFFSET ((:page - 1) * :pageSize)")
    abstract fun getQueuedTweets(account: Int, page: Int = 1, pageSize: Int = 200) : List<QueuedTweet>

    @Ignore
    fun create(text: String, account: Int) : QueuedTweet {
        return this.insert(QueuedTweet(text, account))
    }
}