package com.klinker.android.twitter_l.data.roomdb

import androidx.room.*
import twitter4j.Status


@Entity(tableName = "favorite_tweets", indices = [Index(value = ["tweet_id", "account"], unique = true)])
data class FavoriteTweet(
        @Embedded val tweet: Tweet,
        val account: Int,
        @ColumnInfo(name = "is_unread") val isUnread: Boolean = false,
        @PrimaryKey(autoGenerate = true) val id: Long? = null
) {
    constructor(status: Status, account: Int, isUnread: Boolean = false) : this(Tweet(status), account, isUnread)
}

@Dao
abstract class FavoriteTweetDao : BaseDao<FavoriteTweet>() {

    @Query("SELECT * FROM favorite_tweets WHERE account = :account ORDER BY tweet_id DESC LIMIT :pageSize OFFSET ((:page - 1) * :pageSize)")
    abstract fun getFavoriteTweets(account: Int, page: Int = 1, pageSize: Int = 200) : List<FavoriteTweet>

    @Query("DELETE FROM favorite_tweets WHERE account = :account AND id NOT IN (SELECT id FROM favorite_tweets WHERE account = :account ORDER BY id DESC LIMIT :size)")
    abstract fun trimDatabase(account: Int, size: Int)
}

