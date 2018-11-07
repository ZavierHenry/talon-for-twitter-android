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
        indices = [ Index(value = ["sender_id" ]), Index(value = ["recipient_id"]) ],
        foreignKeys = [
            ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["sender_id"], onDelete = ForeignKey.CASCADE),
            ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["recipient_id"])
        ])
class DirectMessage() {

    @PrimaryKey
    var id: Long = 0

    @ColumnInfo
    var account: Int = 0

    @ColumnInfo(name = "sender_id")
    var senderId: Long = 0

    @ColumnInfo(name = "recipient_id")
    var recipientId: Long = 0

    @ColumnInfo
    var time: Long = 0

    @ColumnInfo
    var text: String = ""

    @ColumnInfo(name = "pic_urls")
    var picUrls: String? = null

    @ColumnInfo
    var urls: String? = null

    @ColumnInfo(name = "gif_url")
    var gifUrl: String? = null

    @ColumnInfo(name = "media_length")
    var mediaLength: Long = 0

    init {

    }


    constructor(status: twitter4j.DirectMessageEvent, possibleUsers: List<twitter4j.User>, account: Int) : this() {

        val html = TweetLinkUtils.getLinksInStatus(status)
        val text = html[0]
        val media = html[1]
        val url = html[2]

        var sender: twitter4j.User? = null
        var receiver: twitter4j.User? = null

        for (user in possibleUsers) {

            if (user.id == status.senderId) {
                sender = user
                if (receiver != null) break

            } else if (user.id == status.recipientId) {
                receiver = user
                if (sender != null) break
            }
        }

        if (sender == null || receiver == null) {
            return
        }

        this.account = account
        this.text = text
        this.id = status.id
        this.senderId = sender.id
        this.time = status.createdTimestamp.time
        this.picUrls = media

        val info = TweetLinkUtils.getGIFUrl(status.mediaEntities, url)
        this.gifUrl = info.url
        this.mediaLength = info.duration

        val entities = status.mediaEntities

        if (entities.isNotEmpty()) {
            this.picUrls = entities[0].mediaURL
        }

        //only saves last url?
        //TODO: check if saving only last url is intended behavior
        val urls = status.urlEntities
        for (u in urls) {
            Log.v("inserting_dm", "url here: " + u.expandedURL)
            this.urls = u.expandedURL
        }

    }


}
