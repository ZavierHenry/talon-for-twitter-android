package com.klinker.android.twitter_l.data.roomdb

import androidx.room.*
import twitter4j.Status


@Entity(tableName = "favorite_tweets")
data class FavoriteTweet(
        @Embedded val tweet: Tweet,
        val account: Int,
        @PrimaryKey(autoGenerate = true) val id: Long? = null
) {
    constructor(status: Status, account: Int) : this(Tweet(status), account)
}

@Dao
interface FavoriteTweetDao {
    @Insert
    fun insertFavoriteTweet(favoriteTweet: FavoriteTweet) : Long?

    @Update
    fun updateFavoriteTweet(favoriteTweet: FavoriteTweet)

    @Delete
    fun deleteFavoriteTweet(favoriteTweet: FavoriteTweet)

    @Query("SELECT * FROM favorite_tweets WHERE account = :account ORDER BY tweet_id ASC LIMIT :pageSize OFFSET ((:page - 1) * :pageSize)")
    fun getFavoriteTweets(account: Int, page: Int = 1, pageSize: Int = 200) : List<FavoriteTweet>
}

