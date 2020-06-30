package com.klinker.android.twitter_l.mockentities.transferentities

import android.content.ContentValues
import com.klinker.android.twitter_l.data.roomdb.HomeTweet
import com.klinker.android.twitter_l.data.sq_lite.HomeSQLiteHelper
import com.klinker.android.twitter_l.mockentities.MockHomeTweet

data class MockTransferHomeTweet(override val mockEntity: MockHomeTweet) : MockTransferEntity<MockHomeTweet> {
    private val homeTweet = mockEntity.homeTweet

    override fun copyId(id: Long): MockTransferEntity<MockHomeTweet> {
        return this.copy(mockEntity = mockEntity.copy(homeTweet = homeTweet.copy(id = id)))
    }

    override fun toSQLiteContentValues(id: Long): ContentValues {
        val tweet = homeTweet.tweet
        return ContentValues().apply {
            put(HomeSQLiteHelper.COLUMN_ACCOUNT, homeTweet.account)
            put(HomeSQLiteHelper.COLUMN_TEXT, tweet.text)
            put(HomeSQLiteHelper.COLUMN_TWEET_ID, tweet.tweetId)
            put(HomeSQLiteHelper.COLUMN_NAME, tweet.author.name)
            put(HomeSQLiteHelper.COLUMN_PRO_PIC, tweet.author.profilePic)
            put(HomeSQLiteHelper.COLUMN_SCREEN_NAME, tweet.author.screenName)
            put(HomeSQLiteHelper.COLUMN_TIME, tweet.time)
            put(HomeSQLiteHelper.COLUMN_RETWEETER, tweet.retweeter?.screenName ?: "")

            put(HomeSQLiteHelper.COLUMN_UNREAD, homeTweet.isUnread)
            put(HomeSQLiteHelper.COLUMN_PIC_URL, tweet.images.joinToString(" "))
            put(HomeSQLiteHelper.COLUMN_URL, tweet.urls.joinToString("  "))
            put(HomeSQLiteHelper.COLUMN_USERS, tweet.mentions.joinToString("  "))
            put(HomeSQLiteHelper.COLUMN_HASHTAGS, tweet.hashtags.joinToString("  "))

            put(HomeSQLiteHelper.COLUMN_CLIENT_SOURCE, tweet.source)
            put(HomeSQLiteHelper.COLUMN_CONVERSATION, tweet.isReply)
            put(HomeSQLiteHelper.COLUMN_ANIMATED_GIF, tweet.gifUrl)
            put(HomeSQLiteHelper.COLUMN_MEDIA_LENGTH, tweet.mediaLength)

            put(HomeSQLiteHelper.COLUMN_CURRENT_POS, homeTweet.isCurrentPosition)

        }
    }
}