package com.klinker.android.twitter_l

import android.content.ContentValues
import android.database.Cursor
import com.klinker.android.twitter_l.data.roomdb.Draft


data class MockDraft(val draft: Draft) : MockEntity {

    override fun toContentValues(): ContentValues {
        return ContentValues().apply {
            put("text", draft.text)
            put("account", draft.account)
        }
    }

    companion object {
        fun toMockDraft(cursor: Cursor) : MockDraft {
            val text = cursor.getString(cursor.getColumnIndex("text"))
            val account = cursor.getInt(cursor.getColumnIndex("account"))
            val id = cursor.getLong(cursor.getColumnIndex("id"))
            return MockDraft(Draft(text, account, id))
        }
    }
}