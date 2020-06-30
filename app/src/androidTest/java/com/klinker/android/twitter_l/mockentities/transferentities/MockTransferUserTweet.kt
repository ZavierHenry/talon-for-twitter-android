package com.klinker.android.twitter_l.mockentities.transferentities

import android.content.ContentValues
import com.klinker.android.twitter_l.data.sq_lite.UserTweetsSQLiteHelper
import com.klinker.android.twitter_l.mockentities.MockUserTweet
import twitter4j.User

data class MockTransferUserTweet(override val mockEntity: MockUserTweet) : MockTransferEntity<MockUserTweet> {
    private val userTweet = mockEntity.userTweet

    override fun copyId(id: Long): MockTransferEntity<MockUserTweet> {
        return this.copy(mockEntity = mockEntity.copy(userTweet = userTweet.copy(id = id)))
    }

    override fun toSQLiteContentValues(id: Long): ContentValues {
        val tweet = userTweet.tweet
        return ContentValues().apply {
            put(UserTweetsSQLiteHelper.COLUMN_TEXT, tweet.text)
            put(UserTweetsSQLiteHelper.COLUMN_TWEET_ID, tweet.tweetId)
            put(UserTweetsSQLiteHelper.COLUMN_NAME, tweet.author.name)
            put(UserTweetsSQLiteHelper.COLUMN_PRO_PIC, tweet.author.profilePic)
            put(UserTweetsSQLiteHelper.COLUMN_SCREEN_NAME, tweet.author.screenName)
            put(UserTweetsSQLiteHelper.COLUMN_TIME, tweet.time)
            put(UserTweetsSQLiteHelper.COLUMN_RETWEETER, tweet.retweeter?.screenName ?: "")

            put(UserTweetsSQLiteHelper.COLUMN_PIC_URL, tweet.images.joinToString(" "))
            put(UserTweetsSQLiteHelper.COLUMN_URL, tweet.urls.joinToString("  "))
            put(UserTweetsSQLiteHelper.COLUMN_USERS, tweet.mentions.joinToString("  "))
            put(UserTweetsSQLiteHelper.COLUMN_HASHTAGS, tweet.hashtags.joinToString("  "))

            put(UserTweetsSQLiteHelper.COLUMN_USER_ID, userTweet.userId)

            put(UserTweetsSQLiteHelper.COLUMN_ANIMATED_GIF, tweet.gifUrl)
            put(UserTweetsSQLiteHelper.COLUMN_MEDIA_LENGTH, tweet.mediaLength)
        }
    }
}