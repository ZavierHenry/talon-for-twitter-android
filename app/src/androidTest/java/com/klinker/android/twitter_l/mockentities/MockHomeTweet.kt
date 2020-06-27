package com.klinker.android.twitter_l.mockentities

import android.content.ContentValues
import android.database.Cursor
import com.klinker.android.twitter_l.data.roomdb.HomeTweet
import com.klinker.android.twitter_l.data.roomdb.Tweet

data class MockHomeTweet(val homeTweet: HomeTweet) : MockEntity {

    override val id get() = homeTweet.id

    constructor(account: Int, isUnread: Boolean = true, isCurrentPosition: Boolean = false, tweet: Tweet = MockUtilities.makeMockTweet(), id: Long = 0) :
            this(HomeTweet(tweet, account, isUnread, isCurrentPosition, id))

    constructor(cursor: Cursor) : this(
            cursor.getInt(cursor.getColumnIndex("account")),
            cursor.getInt(cursor.getColumnIndex("is_unread")) == 1,
            cursor.getInt(cursor.getColumnIndex("is_current_position")) == 1,
            MockUtilities.cursorToTweet(cursor),
            cursor.getLong(cursor.getColumnIndex("id"))
    )

    override fun toContentValues(): ContentValues {
        val tweet = MockUtilities.tweetToContentValues(homeTweet.tweet)
        return ContentValues().apply {
            putAll(tweet)
            put("account", homeTweet.account)
            put("is_unread", homeTweet.isUnread)
            put("is_current_position", homeTweet.isCurrentPosition)
        }
    }

}