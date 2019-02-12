package com.klinker.android.twitter_l.data.roomdb.entities


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import twitter4j.Status

@Entity(tableName = "list_tweets",
        indices = [Index(value = ["list_id", "tweet_id"], unique = true)],
        foreignKeys = [ForeignKey(entity = TweetInteraction::class, parentColumns = ["id"], childColumns = ["tweet_id"], onDelete = ForeignKey.CASCADE)])
class ListTweet {


    @PrimaryKey(autoGenerate = true)
    var id: Long? = null

    @ColumnInfo(name = "tweet_id")
    var tweetId: Long = 0

    @ColumnInfo(name = "list_id")
    var listId: Long = 0

    constructor(id: Long, tweetId: Long, listId: Long) {
        this.id = id
        this.tweetId = tweetId
        this.listId = listId
    }

    constructor(status: Status, listId: Long) {
        this.tweetId = status.id
        this.listId = listId
    }


}
