package com.klinker.android.twitter_l.mockentities

import android.content.ContentValues
import android.database.Cursor
import com.klinker.android.twitter_l.data.roomdb.ListTweet
import com.klinker.android.twitter_l.data.roomdb.Tweet


data class MockListTweet(val listTweet: ListTweet) : MockEntity {

    override val id: Long?
        get() = listTweet.id

    constructor(account: Int, listId: Long = 1L, tweet: Tweet = MockUtilities.makeMockTweet(), id: Long? = null) :
            this(ListTweet(tweet, listId, account, id))

    constructor(cursor: Cursor) : this(
            cursor.getInt(cursor.getColumnIndex("account")),
            cursor.getLong(cursor.getColumnIndex("list_id")),
            MockUtilities.cursorToTweet(cursor),
            cursor.getLong(cursor.getColumnIndex("id"))
    )

    override fun toContentValues(): ContentValues {
        val tweet = MockUtilities.tweetToContentValues(listTweet.tweet)
        return ContentValues().apply {
            putAll(tweet)
            put("list_id", listTweet.listId)
            put("account", listTweet.account)
        }
    }

}