package com.klinker.android.twitter_l.data.roomdb.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import twitter4j.Status


@Entity(tableName = "user_tweets",
        indices = [ Index(value = ["user_id", "tweet_id"], unique = true)])
class UserTweet {

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null

    @ColumnInfo(name = "user_id")
    var userId: Long = 0

    @ColumnInfo(name = "tweet_id")
    var tweetId: Long = 0


    constructor(id: Long, userId: Long, tweetId: Long) {
        this.id = id
        this.userId = userId
        this.tweetId = tweetId
    }

    constructor(status: Status) {

        this.id = null
        this.tweetId = status.id

        if (status.isRetweet) {
            this.userId = status.retweetedStatus.user.id
        } else {
            this.userId = status.user.id
        }

    }


}
