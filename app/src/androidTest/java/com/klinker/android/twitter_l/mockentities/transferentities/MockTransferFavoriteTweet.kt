package com.klinker.android.twitter_l.mockentities.transferentities

import android.content.ContentValues
import com.klinker.android.twitter_l.data.roomdb.Tweet
import com.klinker.android.twitter_l.data.sq_lite.FavoriteTweetsSQLiteHelper
import com.klinker.android.twitter_l.mockentities.MockFavoriteTweet
import com.klinker.android.twitter_l.mockentities.MockUtilities

data class MockTransferFavoriteTweet(override val mockEntity: MockFavoriteTweet) : MockTransferEntity<MockFavoriteTweet> {
    private val favoriteTweet = mockEntity.favoriteTweet

    constructor(account: Int, tweet: Tweet = MockUtilities.makeMockTweet(), isUnread: Boolean = false, id : Long = 0) :
            this(MockFavoriteTweet(account, tweet, isUnread, id))

    override fun copyId(id: Long): MockTransferEntity<MockFavoriteTweet> {
        return this.copy(mockEntity = mockEntity.copy(favoriteTweet = favoriteTweet.copy(id = id)))
    }

    override fun toSQLiteContentValues(id: Long): ContentValues {
        val tweet = favoriteTweet.tweet

        return ContentValues().apply {
            put(FavoriteTweetsSQLiteHelper.COLUMN_ACCOUNT, favoriteTweet.account)
            put(FavoriteTweetsSQLiteHelper.COLUMN_TEXT, tweet.text)
            put(FavoriteTweetsSQLiteHelper.COLUMN_TWEET_ID, tweet.tweetId)
            put(FavoriteTweetsSQLiteHelper.COLUMN_NAME, tweet.author.name)
            put(FavoriteTweetsSQLiteHelper.COLUMN_SCREEN_NAME, tweet.author.screenName)
            put(FavoriteTweetsSQLiteHelper.COLUMN_PRO_PIC, tweet.author.profilePic)
            put(FavoriteTweetsSQLiteHelper.COLUMN_TIME, tweet.time)
            put(FavoriteTweetsSQLiteHelper.COLUMN_RETWEETER, tweet.retweeter?.screenName ?: "")
            put(FavoriteTweetsSQLiteHelper.COLUMN_UNREAD, favoriteTweet.isUnread)
            put(FavoriteTweetsSQLiteHelper.COLUMN_PIC_URL, tweet.images.joinToString(" "))
            put(FavoriteTweetsSQLiteHelper.COLUMN_URL, tweet.urls.joinToString("  "))
            put(FavoriteTweetsSQLiteHelper.COLUMN_USERS, tweet.mentions.joinToString("  "))
            put(FavoriteTweetsSQLiteHelper.COLUMN_HASHTAGS, tweet.hashtags.joinToString("  "))
            put(FavoriteTweetsSQLiteHelper.COLUMN_CLIENT_SOURCE, tweet.source)
            put(FavoriteTweetsSQLiteHelper.COLUMN_CONVERSATION, tweet.isReply)
            put(FavoriteTweetsSQLiteHelper.COLUMN_ANIMATED_GIF, tweet.gifUrl)
            put(FavoriteTweetsSQLiteHelper.COLUMN_MEDIA_LENGTH, tweet.mediaLength ?: -1)
        }
    }
}