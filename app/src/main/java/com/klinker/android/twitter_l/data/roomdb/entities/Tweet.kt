package com.klinker.android.twitter_l.data.roomdb.entities


import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import twitter4j.Status


@Entity(tableName = "tweets",
        foreignKeys = [
            ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["user_id"], onDelete = ForeignKey.CASCADE)
        ])
class Tweet() {

    @PrimaryKey
    var id: Long = 0

    @ColumnInfo
    var text: String = ""

    @ColumnInfo(name = "user_id")
    var userId: Long = 0

    @ColumnInfo
    var time: Long = 0

    @ColumnInfo
    var urls: String? = null

    @ColumnInfo
    var users: String? = null

    @ColumnInfo(name = "picture_urls")
    var pictureUrls: String? = null

    @ColumnInfo
    var retweeter: String = ""

    @ColumnInfo(name = "gif_url")
    var gifUrl: String? = null

    @ColumnInfo(name = "is_conversation")
    var isConversation: Boolean = false

    @ColumnInfo(name = "media_length")
    var mediaLength: Int = 0

    @ColumnInfo(name = "like_count")
    var likeCount: Int = 0

    @ColumnInfo(name = "retweet_count")
    var retweetCount: Int = 0

    @ColumnInfo(name = "is_liked")
    var isLiked: Boolean = false

    @ColumnInfo(name = "is_retweeted")
    var isRetweeted: Boolean = false

    @ColumnInfo(name = "client_source")
    var clientSource: String? = null

    @ColumnInfo
    var hashtags: String? = null

    init {

    }

    constructor(status: Status) : this() {


        this.id = status.id
        this.time = status.createdAt.time
        this.retweeter = ""


        if (status.isRetweet) {
            this.retweeter = status.user.screenName
            this.likeCount = status.retweetedStatus.favoriteCount
            this.retweetCount = status.retweetedStatus.retweetCount

        } else {

            this.retweeter = ""
            this.likeCount = status.favoriteCount
            this.retweetCount = status.retweetCount

        }

    }

}

