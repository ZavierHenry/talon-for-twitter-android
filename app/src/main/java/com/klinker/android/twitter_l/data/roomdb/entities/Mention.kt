package com.klinker.android.twitter_l.data.roomdb.entities


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

import twitter4j.Status


@Entity(tableName = "mentions",
        indices = [Index(value = ["account", "tweet_id"], unique = true), Index(value = ["tweet_id"])],
        foreignKeys = [ForeignKey(entity = Tweet::class, parentColumns = ["id"], childColumns = ["tweet_id"], onDelete = ForeignKey.RESTRICT)])
data class Mention(@PrimaryKey(autoGenerate = true) val id: Long? = null,
                   @field:ColumnInfo val account: Int = -1,
                   @field:ColumnInfo(name = "tweet_id") val tweetId: Long = 0,
                   @field:ColumnInfo(name = "is_unread") val isUnread: Boolean = true) {

    constructor(status: Status, account: Int) : this(null, account, status.id, true)

}





