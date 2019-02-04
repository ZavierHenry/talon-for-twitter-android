package com.klinker.android.twitter_l.data.roomdb.entities


import androidx.room.*

@Entity(tableName = "favorite_user_notifications",
        indices = [ Index(value = ["tweet_id"]) ])
data class FavoriteUserNotification(@PrimaryKey(autoGenerate = true) var id: Long?,
                                    @ColumnInfo(name = "tweet_id") val tweetId: Long)