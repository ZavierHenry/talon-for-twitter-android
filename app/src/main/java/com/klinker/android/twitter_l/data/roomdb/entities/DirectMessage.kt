package com.klinker.android.twitter_l.data.roomdb.entities


import android.util.Log
import androidx.room.*

import com.klinker.android.twitter_l.utils.TweetLinkUtils

import twitter4j.MediaEntity
import twitter4j.URLEntity


@Entity(tableName = "direct_messages",
        indices = [Index(value = ["message_id", "account"], unique = true)])
data class DirectMessage(@PrimaryKey val id: Long? = null,
                         @ColumnInfo(name = "message_id") val messageId: Long = 0,
                         @Embedded(prefix = "sender_") val sender: User? = null,
                         @Embedded(prefix = "recipient_") val recipient: User? = null,
                         @ColumnInfo val account: Int = -1,
                         @ColumnInfo val time: Long = 0,
                         @ColumnInfo val text: String = "",
                         @ColumnInfo(name = "picture_urls") val pictureUrls: List<String> = emptyList(),
                         @ColumnInfo val urls: List<String> = emptyList(),
                         @ColumnInfo(name = "gif_url") val gifUrl: String? = null,
                         @ColumnInfo(name = "media_length") val mediaLength: Long = -1) {


    constructor(status: twitter4j.DirectMessageEvent, possibleUsers: List<twitter4j.User>, account: Int) : this()

}
