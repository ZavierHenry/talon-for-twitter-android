package com.klinker.android.twitter_l.data.roomdb

import androidx.room.*
import twitter4j.Status


@Entity(tableName = "home_tweets")
data class HomeTweet(
        @Embedded val tweet: Tweet,
        val account: Int,
        @ColumnInfo(name = "is_unread") val isUnread: Boolean,
        @PrimaryKey(autoGenerate = true) val id: Long? = null
) {
    constructor(status: Status, account: Int, isUnread: Boolean) : this(Tweet(status), account, isUnread)
}


@Dao
interface HomeTweetDao {

    @Insert
    fun insertHomeTweet(homeTweet: HomeTweet) : Long?

    @Update
    fun updateHomeTweet(homeTweet: HomeTweet)

    @Delete
    fun deleteHomeTweet(homeTweet: HomeTweet)

    @Query("SELECT * FROM home_tweets WHERE account = :account ORDER BY tweet_id DESC LIMIT :pageSize OFFSET ((:page - 1) * :pageSize)")
    fun getHomeTweets(account: Int, page: Int = 1, pageSize: Int = 200) : List<HomeTweet>


}