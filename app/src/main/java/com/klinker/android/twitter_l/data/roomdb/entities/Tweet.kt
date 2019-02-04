package com.klinker.android.twitter_l.data.roomdb.entities


import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.klinker.android.twitter_l.utils.TweetLinkUtils
import twitter4j.Status


@Entity(tableName = "tweets",
        foreignKeys = [
            ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["user_id"], onDelete = ForeignKey.CASCADE)
        ])
data class Tweet(@PrimaryKey val id: Long = 0,
            //possibly make a surrogate key for twitter id to be consistent with user model
            @ColumnInfo val text: String = "",
            @ColumnInfo(name = "user_id") val userId: Long = 0,
            @ColumnInfo val time: Long = 0,
            @ColumnInfo val urls: String? = null,
            @ColumnInfo val users: String? = null,
            @ColumnInfo(name = "picture_urls") val pictureUrls: String? = null,
            @ColumnInfo val retweeter: String = "",
            @ColumnInfo(name = "gif_url") val gifUrl: String? = null,
            @ColumnInfo(name = "media_length") val mediaLength: Int = -1,
            @ColumnInfo(name = "is_conversation") val isConversation: Boolean = false,
            @ColumnInfo(name = "like_count") val likeCount: Int = 0,
            @ColumnInfo(name = "retweet_count") val retweetCount: Int = 0,
            @ColumnInfo(name = "client_source") val clientSource: String? = null,
            @ColumnInfo val hashtags: String? = null) {

    init {

    }


    constructor(status: Status) : this()

}

