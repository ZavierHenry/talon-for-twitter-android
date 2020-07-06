package com.klinker.android.twitter_l.mockentities

import android.content.ContentValues
import android.database.Cursor
import com.klinker.android.twitter_l.data.roomdb.FavoriteUserNotification

data class MockFavoriteUserNotification(val favoriteUserNotification: FavoriteUserNotification) : MockEntity {

    override val id: Long
        get() = favoriteUserNotification.id

    constructor(tweetId: Long = 1L, id: Long = 0) : this(FavoriteUserNotification(tweetId, id))

    constructor(cursor: Cursor) : this(
            cursor.getLong(cursor.getColumnIndex("tweet_id")),
            cursor.getLong(cursor.getColumnIndex("id"))
    )

    override fun toContentValues(): ContentValues {
        return ContentValues().apply {
            put("tweet_id", favoriteUserNotification.tweetId)
        }
    }
}