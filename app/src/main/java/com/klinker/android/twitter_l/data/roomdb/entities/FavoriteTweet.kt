package com.klinker.android.twitter_l.data.roomdb.entities


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.Insert
import androidx.room.PrimaryKey
import twitter4j.Status

@Entity(tableName = "favorite_tweets",
        indices = [Index(value = ["tweet_id", "account"])],
        foreignKeys = [ForeignKey(entity = Tweet::class, parentColumns = ["id"], childColumns = ["tweet_id"], onDelete = ForeignKey.CASCADE)])
class FavoriteTweet {

    @PrimaryKey
    var id: Long? = null

    @ColumnInfo
    var account: Int = 0

    @ColumnInfo(name = "is_unread")
    var isUnread: Boolean = false

    @ColumnInfo(name = "tweet_id")
    var tweetId: Long = 0

    constructor(id: Long? = null, account: Int, isUnread: Boolean, tweetId: Long) {
        this.id = id
        this.account = account
        this.isUnread = isUnread
        this.tweetId = tweetId
    }

    constructor(status: Status, account: Int) {

        this.id = null
        this.account = account
        this.isUnread = true
        this.tweetId = status.id
    }

}
