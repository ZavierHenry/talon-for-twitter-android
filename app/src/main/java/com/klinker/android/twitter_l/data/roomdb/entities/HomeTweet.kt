package com.klinker.android.twitter_l.data.roomdb.entities


import androidx.media.AudioAttributesCompat
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import twitter4j.Status


@Entity(tableName = "home_tweets",
        indices = [Index(value = ["tweet_id", "account"], unique = true)])
class HomeTweet {

    @PrimaryKey(autoGenerate = true)
    val id: Long?

    @ColumnInfo
    val account: Int

    @ColumnInfo(name = "is_unread")
    var isUnread: Boolean = false

    @ColumnInfo(name = "tweet_id")
    val tweetId: Long

    @ColumnInfo(name = "is_current_pos")
    var isCurrentPos: Boolean = false


    constructor(id: Long, account: Int, isUnread: Boolean, tweetId: Long, isCurrentPos: Boolean) {
        this.id = id
        this.account = account
        this.isUnread = isUnread
        this.tweetId = tweetId
        this.isCurrentPos = isCurrentPos
    }


    constructor(status: Status, account: Int) {
        this.id = null
        this.account = account
        this.tweetId = status.id
        this.isUnread = true
        this.isCurrentPos = false
    }


}
