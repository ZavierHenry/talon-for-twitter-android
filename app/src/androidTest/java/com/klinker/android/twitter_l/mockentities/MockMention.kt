package com.klinker.android.twitter_l.mockentities

import android.content.ContentValues
import android.database.Cursor
import com.klinker.android.twitter_l.data.roomdb.Mention
import com.klinker.android.twitter_l.data.roomdb.Tweet


data class MockMention(val mention: Mention) : MockEntity {

    override val id: Long
        get() = mention.id

    constructor(account: Int, isUnread: Boolean = true, tweet: Tweet = MockUtilities.makeMockTweet(), id: Long = 0) :
            this(Mention(tweet, account, isUnread, id))

    constructor(cursor: Cursor) : this(
            cursor.getInt(cursor.getColumnIndex("account")),
            cursor.getInt(cursor.getColumnIndex("is_unread")) == 1,
            MockUtilities.cursorToTweet(cursor),
            cursor.getLong(cursor.getColumnIndex("id"))
    )

    override fun toContentValues(): ContentValues {
        val tweet = MockUtilities.tweetToContentValues(mention.tweet)
        return ContentValues().apply {
            putAll(tweet)
            put("account", mention.account)
            put("is_unread", mention.isUnread)
        }
    }

}