package com.klinker.android.twitter_l

import android.content.ContentValues
import android.database.Cursor
import com.klinker.android.twitter_l.data.roomdb.Tweet
import com.klinker.android.twitter_l.data.roomdb.UserTweet

data class MockUserTweet(val userTweet: UserTweet) : MockEntity {

    constructor(account: Int, tweet: Tweet = MockUtilities.makeMockTweet(), id: Long? = null) :
            this(UserTweet(tweet, account, id))

    constructor(cursor: Cursor) : this(
            cursor.getInt(cursor.getColumnIndex("account")),
            MockUtilities.cursorToTweet(cursor),
            cursor.getLong(cursor.getColumnIndex("id"))
    )

    override fun toContentValues(): ContentValues {
        val tweet = MockUtilities.tweetToContentValues(userTweet.tweet)
        return ContentValues().apply {
            putAll(tweet)
            put("account", userTweet.account)
        }
    }
}