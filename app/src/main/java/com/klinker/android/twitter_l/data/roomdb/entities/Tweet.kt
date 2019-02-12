package com.klinker.android.twitter_l.data.roomdb.entities


import android.os.Build
import android.text.Html
import androidx.annotation.NonNull
import androidx.room.*
import com.klinker.android.twitter_l.utils.TweetLinkUtils
import twitter4j.Status


data class Tweet(
            @Embedded(prefix = "user_") val tweeter: User,
            @ColumnInfo(name = "twitter_id") val twitterId: Long,
            @ColumnInfo val text: String = "",
            @ColumnInfo val time: Long = 0,
            @ColumnInfo val urls: List<String> = emptyList(),
            @ColumnInfo(name = "mentioned_users") val users: List<String> = emptyList(),
            @ColumnInfo(name = "picture_urls") val pictureUrls: List<String> = emptyList(),
            @ColumnInfo val retweeter: String = "",
            @ColumnInfo(name = "gif_url") val gifUrl: String? = null,
            @ColumnInfo(name = "media_length") val mediaLength: Long = -1L,
            @ColumnInfo(name = "is_conversation") val isConversation: Boolean = false,
            @ColumnInfo(name = "like_count") val likeCount: Int = 0,
            @ColumnInfo(name = "retweet_count") val retweetCount: Int = 0,
            @ColumnInfo(name = "client_source") val clientSource: String? = null,
            @ColumnInfo val hashtags: List<String> = emptyList()) {


    private constructor(time: Long, id: Long, retweeter: String, originalStatus: Status, linksInStatus: Array<String>, gifInfo: TweetLinkUtils.TweetMediaInformation) : this(
            User(originalStatus.user),
            id,
            linksInStatus[0],
            time,
            linksInStatus[2].split(" "),
            linksInStatus[4].split(" "),
            linksInStatus[1].let { media ->
                if (media.contains("/tweet_video/")) {
                    media.replace("tweet_video", "tweet_video_thumb")
                            .replace(".mp4", ".png")
                            .replace(".m3u8", ".png")
                            .split(" ")
                } else {
                    media.split(" ")
                }
            },
            retweeter,
            gifInfo.url,
            gifInfo.duration,
            originalStatus.inReplyToStatusId != -1L,
            originalStatus.favoriteCount,
            originalStatus.retweetCount,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                android.text.Html.fromHtml(originalStatus.source, Html.FROM_HTML_MODE_LEGACY).toString()
            } else {
                android.text.Html.fromHtml(originalStatus.source).toString()
            },
            linksInStatus[3].split(" ")
    )


    private constructor(status: Status, linksInStatus: Array<String>) : this(
            status.createdAt.time,
            status.id,
            if (status.isRetweet) status.user.screenName else "",
            if (status.isRetweet) status.retweetedStatus else status,
            linksInStatus,
            TweetLinkUtils.getGIFUrl(if (status.isRetweet) status.retweetedStatus else status, linksInStatus[2])
    )

    constructor(status: Status) : this(status, TweetLinkUtils.getLinksInStatus(if (status.isRetweet) status.retweetedStatus else status))

}

