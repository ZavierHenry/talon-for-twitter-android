package com.klinker.android.twitter_l.data.roomdb.entities


import android.util.Log

import com.klinker.android.twitter_l.utils.TweetLinkUtils

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import twitter4j.MediaEntity
import twitter4j.URLEntity


@Entity(tableName = "direct_messages",
        indices = [
            Index(value = ["sender_id"]),
            Index(value = ["recipient_id"]),
            Index(value = ["message_id", "account"], unique = true)],
        foreignKeys = [
            ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["sender_id"], onDelete = ForeignKey.RESTRICT),
            ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["recipient_id"], onDelete = ForeignKey.RESTRICT)
        ])
data class DirectMessage(@PrimaryKey val id: Long? = null,
                         @ColumnInfo val account: Int = -1,
                         @ColumnInfo(name = "sender_id") val senderId: Long = 0,
                         @ColumnInfo(name = "recipient_id") val recipientId: Long = 0,
                         @ColumnInfo(name = "message_id") val messageId: Long = 0,
                         @ColumnInfo val time: Long = 0,
                         @ColumnInfo val text: String = "",
                         @ColumnInfo(name = "picture_urls") val pictureUrls: String? = null,
                         @ColumnInfo val urls: String? = null,
                         @ColumnInfo(name = "gif_url") val gifUrl: String? = null,
                         @ColumnInfo(name = "media_length") val mediaLength: Long = -1) {


    constructor(status: twitter4j.DirectMessageEvent, possibleUsers: List<twitter4j.User>, account: Int) : this()

}
