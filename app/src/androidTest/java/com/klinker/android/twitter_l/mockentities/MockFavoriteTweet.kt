package com.klinker.android.twitter_l.mockentities

import android.content.ContentValues
import android.database.Cursor
import com.klinker.android.twitter_l.data.roomdb.FavoriteTweet
import com.klinker.android.twitter_l.data.roomdb.Tweet

data class MockFavoriteTweet(val favoriteTweet: FavoriteTweet) : MockEntity {

    constructor(account: Int, tweet: Tweet = MockUtilities.makeMockTweet(), isUnread: Boolean = false, id: Long? = null) :
            this(FavoriteTweet(tweet, account, isUnread, id))

    constructor(cursor: Cursor) : this(
            cursor.getInt(cursor.getColumnIndex("account")),
            MockUtilities.cursorToTweet(cursor),
            cursor.getInt(cursor.getColumnIndex("is_unread")) == 1,
            cursor.getLong(cursor.getColumnIndex("id"))
    )

    override fun toContentValues(): ContentValues {
        val tweet = MockUtilities.tweetToContentValues(favoriteTweet.tweet)
        return ContentValues().apply {
            putAll(tweet)
            put("account", favoriteTweet.account)
        }
    }

}