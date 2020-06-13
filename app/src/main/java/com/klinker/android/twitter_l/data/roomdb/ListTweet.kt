package com.klinker.android.twitter_l.data.roomdb

import androidx.room.*
import twitter4j.Status


@Entity(tableName = "list_tweets")
data class ListTweet(
        @Embedded val tweet: Tweet,
        @ColumnInfo(name = "list_id") val listId: Long,
        val account: Int,
        @PrimaryKey(autoGenerate = true) val id: Long? = null
) {
    constructor(status: Status, listId: Long, account: Int) : this(Tweet(status), listId, account)
}


@Dao
interface ListTweetDao {

    @Insert
    fun insertListTweet(listTweet: ListTweet) : Long?

    @Update
    fun updateListTweet(listTweet: ListTweet)

    @Delete
    fun deleteListTweet(listTweet: ListTweet)

    @Query("SELECT * FROM list_tweets WHERE account = :account AND list_id = :listId ORDER BY tweet_id ASC LIMIT :pageSize OFFSET ((:page - 1) * :pageSize)")
    fun getListTweets(account: Int, listId: Long, page: Int = 1, pageSize: Int = 200) : List<ListTweet>
}