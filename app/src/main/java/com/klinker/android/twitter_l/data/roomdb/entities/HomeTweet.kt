package com.klinker.android.twitter_l.data.roomdb.entities


import androidx.room.*
import twitter4j.Status


@Entity(tableName = "home_tweets",
        indices = [Index(value = ["tweet_id", "account"], unique = true)],
        foreignKeys = [ForeignKey(entity = Tweet::class, childColumns = ["tweet_id"], parentColumns = ["id"], onDelete = ForeignKey.RESTRICT)])
data class HomeTweet(@PrimaryKey(autoGenerate = true) val id: Long? = null,
                     @ColumnInfo val account: Int,
                     @ColumnInfo(name = "is_unread") val isUnread: Boolean = false,
                     @ColumnInfo(name = "tweet_id") val tweetId: Long? = null,
                     @ColumnInfo(name = "is_current_pos") val isCurrentPos: Boolean = false,
                     @ColumnInfo(name = "is_liked") val isLiked: Boolean = false,
                     @ColumnInfo(name = "is_retweeted") val isRetweeted: Boolean = false) {


    constructor(status: Status, account: Int, isUnread: Boolean) : this(null, account, isUnread, status.id, false, status.isFavorited, status.isRetweeted)

}
