package com.klinker.android.twitter_l.data.roomdb

import androidx.room.*
import twitter4j.Status


@Entity(tableName = "saved_tweets")
data class SavedTweet(
        @Embedded val tweet: Tweet,
        val account: Int,
        @PrimaryKey(autoGenerate = true) val id: Long? = null
) {

    constructor(status: Status, account: Int) : this(Tweet(status), account)

}

@Dao
interface SavedTweetDao {

    @Insert
    fun insertSavedTweet(savedTweet: SavedTweet) : Long?

    @Delete
    fun deleteSavedTweet(savedTweet: SavedTweet)

    @Query("SELECT * FROM saved_tweets WHERE account = :account ORDER BY tweet_id ASC LIMIT :pageSize OFFSET ((:page - 1) * :pageSize)")
    fun getSavedTweets(account: Int, page: Int = 1, pageSize: Int = 200) : List<SavedTweet>
}