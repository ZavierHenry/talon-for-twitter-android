package com.klinker.android.twitter_l.mockentities

import android.content.ContentValues
import android.database.Cursor
import com.klinker.android.twitter_l.data.roomdb.Draft


data class MockDraft(val draft: Draft) : MockEntity {

    constructor(account: Int, text: String = "", id: Long? = null) : this(Draft(text, account, id))

    constructor(cursor: Cursor) : this(
            cursor.getInt(cursor.getColumnIndex("account")),
            cursor.getString(cursor.getColumnIndex("text")),
            cursor.getLong(cursor.getColumnIndex("id"))
    )

    override fun toContentValues(): ContentValues {
        return ContentValues().apply {
            put("text", draft.text)
            put("account", draft.account)
        }
    }
}