package com.klinker.android.twitter_l.mockentities.transferentities

import android.content.ContentValues
import com.klinker.android.twitter_l.data.sq_lite.ListSQLiteHelper
import com.klinker.android.twitter_l.mockentities.MockListTweet

data class MockTransferListTweet(override val mockEntity: MockListTweet) : MockTransferEntity<MockListTweet> {
    private val listTweet = mockEntity.listTweet

    override fun copyId(id: Long): MockTransferEntity<MockListTweet> {
        return this.copy(mockEntity = mockEntity.copy(listTweet = listTweet.copy(id = id)))
    }

    override fun toSQLiteContentValues(id: Long): ContentValues {
        val tweet = listTweet.tweet
        return ContentValues().apply {
            put(ListSQLiteHelper.COLUMN_TEXT, tweet.text)
            put(ListSQLiteHelper.COLUMN_TWEET_ID, tweet.tweetId)
            put(ListSQLiteHelper.COLUMN_NAME, tweet.author.name)
            put(ListSQLiteHelper.COLUMN_PRO_PIC, tweet.author.profilePic)
            put(ListSQLiteHelper.COLUMN_SCREEN_NAME, tweet.author.screenName)
            put(ListSQLiteHelper.COLUMN_TIME, tweet.time)

            put(ListSQLiteHelper.COLUMN_RETWEETER, tweet.retweeter?.screenName ?: "")

            put(ListSQLiteHelper.COLUMN_PIC_URL, tweet.images.joinToString(" "))
            put(ListSQLiteHelper.COLUMN_URL, tweet.urls.joinToString("  "))
            put(ListSQLiteHelper.COLUMN_USERS, tweet.mentions.joinToString("  "))
            put(ListSQLiteHelper.COLUMN_HASHTAGS, tweet.hashtags.joinToString("  "))

            put(ListSQLiteHelper.COLUMN_LIST_ID, listTweet.listId)
            put(ListSQLiteHelper.COLUMN_ANIMATED_GIF, tweet.gifUrl)
            put(ListSQLiteHelper.COLUMN_MEDIA_LENGTH, tweet.mediaLength)
        }
    }
}