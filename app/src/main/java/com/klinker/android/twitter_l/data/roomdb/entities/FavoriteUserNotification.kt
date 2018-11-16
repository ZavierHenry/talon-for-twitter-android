package com.klinker.android.twitter_l.data.roomdb.entities


import androidx.room.*

@Entity(tableName = "favorite_user_notifications",
        indices = [ Index(value = ["tweet_id"]) ],
        foreignKeys =  [ ForeignKey(entity = Tweet::class, parentColumns = ["id"], childColumns = ["tweet_id"], onDelete = ForeignKey.CASCADE) ])
class FavoriteUserNotification {

    @PrimaryKey(autoGenerate = true)
    var id: Long?

    @ColumnInfo(name = "tweet_id")
    var tweetId: Long

    @Ignore
    constructor(tweetId: Long) {
        this.id = null
        this.tweetId = tweetId
    }

    constructor(id: Long, tweetId: Long) {
        this.id = id
        this.tweetId = tweetId
    }

}
