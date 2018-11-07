package com.klinker.android.twitter_l.data.roomdb.entities


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

import twitter4j.Status


@Entity(tableName = "mentions",
        indices = [Index(value = ["account", "tweet_id"], unique = true)],
        foreignKeys = [ForeignKey(entity = Tweet::class, parentColumns = ["id"], childColumns = ["tweet_id"])])
class Mention {

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null

    @ColumnInfo(name = "account")
    var account: Int = 0

    @ColumnInfo(name = "tweet_id")
    var tweetId: Long = 0


    @ColumnInfo(name = "is_unread", typeAffinity = ColumnInfo.INTEGER)
    var isUnread: Boolean = false


    constructor(id: Long, account: Int, tweetId: Long, isUnread: Boolean) {
        this.id = id
        this.account = account
        this.tweetId = tweetId
        this.isUnread = isUnread
    }


    constructor(status: Status, account: Int) {
        this.id = null
        this.account = account
        this.tweetId = status.id
        this.isUnread = true
    }


}





