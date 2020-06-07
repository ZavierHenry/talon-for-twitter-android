package com.klinker.android.twitter_l.data.roomdb

import androidx.room.*
import twitter4j.Status


@Entity(tableName = "favorite_tweets")
data class FavoriteTweet(
        @Embedded val tweet: Tweet,
        val account: Int,
        @PrimaryKey val id: Long? = null
) {
    constructor(status: Status, account: Int) : this(Tweet(status), account)
}

@Dao
interface FavoriteTweetDao {
    @Insert
    fun insertFavoriteTweet(favoriteTweet: FavoriteTweet)

    @Delete
    fun deleteFavoriteTweet(favoriteTweet: FavoriteTweet)

    @Query("SELECT * FROM favorite_tweets WHERE account = :account")
    fun getFavoriteTweets(account: Int)
}

