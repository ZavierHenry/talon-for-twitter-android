package com.klinker.android.twitter_l.data.roomdb.entities


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.Insert
import androidx.room.PrimaryKey
import twitter4j.Status

@Entity(tableName = "favorite_tweets",
        indices = [Index(value = ["tweet_id", "account"], unique = true)],
        foreignKeys = [ForeignKey(entity = Tweet::class, parentColumns = ["id"], childColumns = ["tweet_id"], onDelete = ForeignKey.RESTRICT)])
data class FavoriteTweet(@PrimaryKey val id: Long? = null,
                         @ColumnInfo val account: Int = -1,
                         @ColumnInfo(name = "tweet_id") val tweetId: Long = 0,
                         @ColumnInfo(name = "is_retweeted") val isRetweeted: Boolean = false,
                         @ColumnInfo(name = "is_unread") val isUnread: Boolean = false) {

    constructor(status: Status, account: Int) : this(null, account, status.id, if (status.isRetweet) status.retweetedStatus.isRetweeted else status.isRetweeted, true)

}
