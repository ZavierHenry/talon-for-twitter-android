package com.klinker.android.twitter_l.mockentities

import android.content.ContentValues
import android.database.Cursor
import com.klinker.android.twitter_l.data.roomdb.Hashtag

data class MockHashtag(val hashtag: Hashtag) : MockEntity {
    override val id: Long?
        get() = hashtag.id

    constructor(tag: String = "#tag", id: Long? = null) : this(Hashtag(tag, id))

    constructor(cursor: Cursor) : this(
            cursor.getString(cursor.getColumnIndex("tag")),
            cursor.getLong(cursor.getColumnIndex("id"))
    )

    override fun toContentValues(): ContentValues {
        return ContentValues().apply {
            put("tag", hashtag.tag)
        }
    }

}