package com.klinker.android.twitter_l.mockentities

import android.content.ContentValues
import android.database.Cursor
import com.klinker.android.twitter_l.data.roomdb.ScheduledTweet

data class MockScheduledTweet(val scheduledTweet: ScheduledTweet) : MockEntity {

    constructor(account: Int, text: String = "", time: Long = 0L, alarmId: Long = 1L, id: Long? = null) :
            this(ScheduledTweet(text, time, alarmId, account, id))

    constructor(cursor: Cursor) : this(
            cursor.getInt(cursor.getColumnIndex("account")),
            cursor.getString(cursor.getColumnIndex("text")),
            cursor.getLong(cursor.getColumnIndex("time")),
            cursor.getLong(cursor.getColumnIndex("alarm_id")),
            cursor.getLong(cursor.getColumnIndex("id"))
    )

    override fun toContentValues(): ContentValues {
        return ContentValues().apply {
            put("text", scheduledTweet.text)
            put("time", scheduledTweet.time)
            put("alarm_id", scheduledTweet.alarmId)
            put("account", scheduledTweet.account)
        }
    }
}