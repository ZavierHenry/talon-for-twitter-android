package com.klinker.android.twitter_l

import android.content.ContentValues
import android.database.Cursor
import com.klinker.android.twitter_l.data.roomdb.QueuedTweet

data class MockQueuedTweet(val queuedTweet: QueuedTweet) : MockEntity {

    constructor(account: Int, text: String = "", id: Long? = null) : this(QueuedTweet(text, account, id))

    constructor(cursor: Cursor) : this(
            cursor.getInt(cursor.getColumnIndex("account")),
            cursor.getString(cursor.getColumnIndex("text")),
            cursor.getLong(cursor.getColumnIndex("id"))
    )

    override fun toContentValues(): ContentValues {
        return ContentValues().apply {
            put("account", queuedTweet.account)
            put("text", queuedTweet.text)
        }
    }

}