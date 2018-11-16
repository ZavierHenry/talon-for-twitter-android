package com.klinker.android.twitter_l.data.roomdb.entities


import com.google.android.gms.wearable.ChannelClient

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "queued_tweets",
        indices = [Index(value = ["text", "account"])])
class QueuedTweet {

    @PrimaryKey(autoGenerate = true)
    val id: Long?

    @ColumnInfo
    var account: Int = 0

    @ColumnInfo
    var text: String

    @Ignore
    constructor(text: String, account: Int) {
        this.id = null
        this.text = text
        this.account = account
    }

    constructor(id: Long, text: String, account: Int) {
        this.id = id
        this.text = text
        this.account = account
    }

}
