package com.klinker.android.twitter_l.data.roomdb.transfers

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import androidx.room.OnConflictStrategy
import androidx.sqlite.db.SupportSQLiteDatabase
import com.klinker.android.twitter_l.data.sq_lite.EmojiSQLiteHelper

class EmojiTransfer(context: Context) : TalonDatabaseCallback(context.getDatabasePath("recent.db"), EmojiSQLiteHelper.TABLE_RECENTS, EmojiSQLiteHelper.COLUMN_ID) {
    override fun onEachTableRow(cursor: Cursor, newDatabase: SupportSQLiteDatabase) {
        val text = cursor.getString(cursor.getColumnIndex(EmojiSQLiteHelper.COLUMN_TEXT))
        val icon = cursor.getString(cursor.getColumnIndex(EmojiSQLiteHelper.COLUMN_ICON))
        val count = cursor.getLong(cursor.getColumnIndex(EmojiSQLiteHelper.COLUMN_COUNT))

        val contentValues = ContentValues().apply {
            put("text", text)
            put("icon", icon)
            put("count", count)
        }

        newDatabase.insert("emojis", OnConflictStrategy.IGNORE, contentValues)
    }

}