package com.klinker.android.twitter_l.mockentities

import android.content.ContentValues
import android.database.Cursor
import com.klinker.android.twitter_l.data.roomdb.Tweet
import com.klinker.android.twitter_l.data.roomdb.UserTweet

data class MockUserTweet(val userTweet: UserTweet) : MockEntity {

    override val id: Long
        get() = userTweet.id

    constructor(userId: Long, tweet: Tweet = MockUtilities.makeMockTweet(), id: Long = 0) :
            this(UserTweet(tweet, userId, id))

    constructor(cursor: Cursor) : this(
            cursor.getLong(cursor.getColumnIndex("account_user_id")),
            MockUtilities.cursorToTweet(cursor),
            cursor.getLong(cursor.getColumnIndex("id"))
    )

    override fun toContentValues(): ContentValues {
        val tweet = MockUtilities.tweetToContentValues(userTweet.tweet)
        return ContentValues().apply {
            putAll(tweet)
            put("account_user_id", userTweet.userId)
        }
    }
}