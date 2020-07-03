package com.klinker.android.twitter_l.data.roomdb

import androidx.room.*
import twitter4j.Status


@Entity(tableName = "saved_tweets")
data class SavedTweet @JvmOverloads constructor(
        @Embedded val tweet: Tweet,
        val account: Int,
        @PrimaryKey(autoGenerate = true) val id: Long = 0
) : BaseDao.TalonEntity<SavedTweet> {
    constructor(status: Status, account: Int) : this(Tweet(status), account)

    override fun copyWithId(id: Long): SavedTweet {
        return this.copy(id = id)
    }
}

@Dao
abstract class SavedTweetDao : BaseDao<SavedTweet>() {

    @Query("SELECT * FROM saved_tweets WHERE account = :account ORDER BY tweet_id ASC LIMIT :pageSize OFFSET ((:page - 1) * :pageSize)")
    abstract fun getSavedTweets(account: Int, page: Int = 1, pageSize: Int = 200) : List<SavedTweet>

    @Ignore
    fun create(status: Status, account: Int) : SavedTweet {
        return this.insert(SavedTweet(status, account))
    }

}