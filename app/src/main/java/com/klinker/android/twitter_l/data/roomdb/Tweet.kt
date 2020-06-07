package com.klinker.android.twitter_l.data.roomdb

import androidx.core.text.HtmlCompat
import androidx.room.ColumnInfo
import androidx.room.Embedded
import com.klinker.android.twitter_l.utils.TweetLinkUtils
import twitter4j.Status

data class Tweet(
        @ColumnInfo(name = "tweet_id") val tweetId: Long,
        @Embedded(prefix = "user_") val author: User,
        val text: String,
        val time: Long,
        val hashtags: List<String>,
        val mentions: List<String>,
        val images: List<String>,
        val urls: List<String>,
        val likes: Int? = null,
        val retweets: Int? = null,
        val liked: Boolean? = null,
        val retweeted: Boolean? = null,
        val source: String? = null,
        @Embedded(prefix = "retweeter_") val retweeter: User? = null,
        @ColumnInfo(name = "gif_url") val gifUrl: String? = null,
        @ColumnInfo(name = "media_length") val mediaLength: Long? = null
) {

    constructor(status: Status) : this(
            status,
            if (status.isRetweet) status.retweetedStatus else status,
            TweetLinkUtils.getLinksInStatus(if (status.isRetweet) status.retweetedStatus else status)
    )

    private constructor(status: Status, originalStatus: Status, linksInStatus: Array<String>, mediaInfo: TweetLinkUtils.TweetMediaInformation) : this(
            status.id,
            User(originalStatus.user),
            linksInStatus[0],
            status.createdAt.time,
            linksInStatus[3].split("|"),
            linksInStatus[4].split("|"),
            linksInStatus[1].split("|").map { media ->
                if (media.contains("/tweet_video/"))
                    media.replace("tweet_video", "tweet_video_thumb")
                            .replace(".mp4", ".png")
                            .replace(".m3u8", ".png")
                else
                    media
            },
            linksInStatus[2].split("|"),
            originalStatus.favoriteCount,
            originalStatus.retweetCount,
            originalStatus.isFavorited,
            originalStatus.isRetweetedByMe,
            HtmlCompat.fromHtml(originalStatus.source, HtmlCompat.FROM_HTML_MODE_LEGACY).toString(),
            if (status.id != originalStatus.id) User(status.user) else null,
            mediaInfo.url,
            if (mediaInfo.duration == -1L) null else mediaInfo.duration
    )

    private constructor(status: Status, originalStatus: Status, linksInStatus: Array<String>) : this(
            status,
            originalStatus,
            linksInStatus,
            TweetLinkUtils.getGIFUrl(originalStatus, linksInStatus[2])
    )

}