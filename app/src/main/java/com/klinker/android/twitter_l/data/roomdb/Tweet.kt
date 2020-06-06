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
        val account: Int,
        val likes: Int? = null,
        val retweets: Int? = null,
        val liked: Boolean? = null,
        val retweeted: Boolean? = null,
        val source: String? = null,
        @Embedded(prefix = "retweeter_") val retweeter: User? = null
) {

    constructor(status: Status, account: Int) : this(
            status,
            if (status.isRetweet) status.retweetedStatus else status,
            TweetLinkUtils.getLinksInStatus(if (status.isRetweet) status.retweetedStatus else status),
            account
    )

    private constructor(status: Status, originalStatus: Status, linksInStatus: Array<String>, account: Int) : this(
            status.id,
            User(originalStatus.user, account),
            linksInStatus[0],
            status.createdAt.time,
            linksInStatus[3].split(" "),
            linksInStatus[4].split(" "),
            linksInStatus[1].split(" "),
            linksInStatus[2].split(" "),
            account,
            originalStatus.favoriteCount,
            originalStatus.retweetCount,
            originalStatus.isFavorited,
            originalStatus.isRetweetedByMe,
            HtmlCompat.fromHtml(originalStatus.source, HtmlCompat.FROM_HTML_MODE_LEGACY).toString(),
            if (status.id != originalStatus.id) User(status.user, account) else null
    )

}