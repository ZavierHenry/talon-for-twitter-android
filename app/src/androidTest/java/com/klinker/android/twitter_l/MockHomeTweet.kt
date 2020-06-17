package com.klinker.android.twitter_l

import android.content.ContentValues
import android.database.Cursor
import com.klinker.android.twitter_l.data.roomdb.HomeTweet
import com.klinker.android.twitter_l.data.roomdb.Tweet

data class MockHomeTweet(val homeTweet: HomeTweet) : MockEntity {

    constructor(account: Int, isUnread: Boolean = true, tweet: Tweet = MockUtilities.makeMockTweet(), id: Long? = null) :
            this(HomeTweet(tweet, account, isUnread, id))

    constructor(cursor: Cursor) : this(
            cursor.getInt(cursor.getColumnIndex("account")),
            cursor.getInt(cursor.getColumnIndex("is_unread")) == 1,
            MockUtilities.cursorToTweet(cursor),
            cursor.getLong(cursor.getColumnIndex("id"))
    )

    override fun toContentValues(): ContentValues {
        val tweet = MockUtilities.tweetToContentValues(homeTweet.tweet)
        return ContentValues().apply {
            putAll(tweet)
            put("account", homeTweet.account)
            put("is_unread", homeTweet.isUnread)
        }
    }

}