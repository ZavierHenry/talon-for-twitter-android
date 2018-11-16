package com.klinker.android.twitter_l.data.roomdb.entities


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import twitter4j.Status


@Entity(tableName = "saved_tweets",
        indices = [ Index(value = ["tweet_id", "account"], unique = true) ],
        foreignKeys = [
            ForeignKey(entity = Tweet::class, parentColumns = ["id"], childColumns = ["tweet_id"], onDelete = ForeignKey.CASCADE)
                ])
class SavedTweet {

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null

    @ColumnInfo
    var account: Int

    @ColumnInfo(name = "tweet_id")
    var tweetId: Long = 0

    constructor(id: Long? = null, account: Int, tweetId: Long) {
        this.id = id
        this.account = account
        this.tweetId = tweetId
    }

    constructor(status: Status, account: Int) {
        this.account = account
        this.tweetId = status.id
    }

}
