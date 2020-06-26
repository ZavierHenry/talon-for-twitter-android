package com.klinker.android.twitter_l.mockentities

import android.content.ContentValues
import android.database.Cursor
import com.klinker.android.twitter_l.data.roomdb.SavedTweet
import com.klinker.android.twitter_l.data.roomdb.Tweet

data class MockSavedTweet(val savedTweet: SavedTweet) : MockEntity {

    override val id: Long
        get() = savedTweet.id

    constructor(account: Int, tweet: Tweet = MockUtilities.makeMockTweet(), id: Long = 0) :
            this(SavedTweet(tweet, account, id))

    constructor(cursor: Cursor) : this(
            cursor.getInt(cursor.getColumnIndex("account")),
            MockUtilities.cursorToTweet(cursor),
            cursor.getLong(cursor.getColumnIndex("id"))
    )

    override fun toContentValues(): ContentValues {
        val tweet = MockUtilities.tweetToContentValues(savedTweet.tweet)
        return ContentValues().apply {
            putAll(tweet)
            put("account", savedTweet.account)
        }
    }

}