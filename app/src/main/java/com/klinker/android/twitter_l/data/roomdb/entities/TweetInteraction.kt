package com.klinker.android.twitter_l.data.roomdb.entities

import androidx.room.*
import twitter4j.Status


@Entity(
        tableName = "tweet_interactions",
        indices = [Index(value = ["tweet_id", "account"], unique = true)])
data class TweetInteraction(
        @Embedded(prefix = "tweet_") val tweet: Tweet,
        @ColumnInfo val account: Int,
        @PrimaryKey val id: Long? = null,
        @ColumnInfo(name = "is_liked") var isLiked: Boolean = false,
        @ColumnInfo(name = "is_retweeted") var isRetweeted: Boolean = false,
        @ColumnInfo(name = "is_mentioned") val isMentioned: Boolean = false,
        @ColumnInfo(name = "is_saved") var isSaved: Boolean = false,
        @ColumnInfo(name = "is_user_tweet") val isUserTweet: Boolean = false,
        @ColumnInfo(name = "is_unread") val isUnread: Boolean = false,
        @ColumnInfo(name = "is_in_home_timeline") val isInHomeTimeline: Boolean = false,
        @ColumnInfo(name = "is_current_home_position") val isCurrentHomePosition: Boolean? = null) {







}

