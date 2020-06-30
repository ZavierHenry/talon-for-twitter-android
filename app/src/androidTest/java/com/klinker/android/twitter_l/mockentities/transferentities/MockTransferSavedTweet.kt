package com.klinker.android.twitter_l.mockentities.transferentities

import android.content.ContentValues
import com.klinker.android.twitter_l.data.roomdb.Tweet
import com.klinker.android.twitter_l.data.sq_lite.SavedTweetSQLiteHelper
import com.klinker.android.twitter_l.mockentities.MockSavedTweet
import com.klinker.android.twitter_l.mockentities.MockUtilities

data class MockTransferSavedTweet(override val mockEntity: MockSavedTweet) : MockTransferEntity<MockSavedTweet> {
    private val savedTweet = mockEntity.savedTweet

    constructor(account: Int, tweet: Tweet = MockUtilities.makeMockTweet(), id: Long = 0) :
            this(MockSavedTweet(account, tweet, id))

    override fun copyId(id: Long): MockTransferSavedTweet {
        return this.copy(mockEntity = mockEntity.copy(savedTweet = savedTweet.copy(id = id)))
    }

    override fun toSQLiteContentValues(id: Long): ContentValues {
        val tweet = savedTweet.tweet
        return ContentValues().apply {
            put(SavedTweetSQLiteHelper.COLUMN_TEXT, tweet.text)
            put(SavedTweetSQLiteHelper.COLUMN_TWEET_ID, tweet.tweetId)
            put(SavedTweetSQLiteHelper.COLUMN_NAME, tweet.author.name)
            put(SavedTweetSQLiteHelper.COLUMN_PRO_PIC, tweet.author.profilePic)
            put(SavedTweetSQLiteHelper.COLUMN_SCREEN_NAME, tweet.author.screenName)
            put(SavedTweetSQLiteHelper.COLUMN_TIME, tweet.time)

            put(SavedTweetSQLiteHelper.COLUMN_RETWEETER, tweet.retweeter?.screenName ?: "")

            put(SavedTweetSQLiteHelper.COLUMN_PIC_URL, tweet.images.joinToString(" "))
            put(SavedTweetSQLiteHelper.COLUMN_URL, tweet.urls.joinToString("  "))
            put(SavedTweetSQLiteHelper.COLUMN_USERS, tweet.mentions.joinToString("  "))
            put(SavedTweetSQLiteHelper.COLUMN_HASHTAGS, tweet.hashtags.joinToString("  "))

            put(SavedTweetSQLiteHelper.COLUMN_ACCOUNT, savedTweet.account)
            put(SavedTweetSQLiteHelper.COLUMN_ANIMATED_GIF, tweet.gifUrl)
            put(SavedTweetSQLiteHelper.COLUMN_MEDIA_LENGTH, tweet.mediaLength)

        }
    }
}