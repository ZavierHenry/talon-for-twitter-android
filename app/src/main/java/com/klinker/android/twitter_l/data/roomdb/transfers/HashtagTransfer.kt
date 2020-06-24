package com.klinker.android.twitter_l.data.roomdb.transfers

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import androidx.room.OnConflictStrategy
import androidx.sqlite.db.SupportSQLiteDatabase
import com.klinker.android.twitter_l.data.sq_lite.HashtagSQLiteHelper

class HashtagTransfer(context: Context) : TalonDatabaseCallback(context.getDatabasePath("hashtags.db"), HashtagSQLiteHelper.TABLE_HASHTAGS) {
    override fun onEachTableRow(cursor: Cursor, newDatabase: SupportSQLiteDatabase) {
        val tag = cursor.getString(cursor.getColumnIndex(HashtagSQLiteHelper.COLUMN_TAG))
        val contentValues = ContentValues().apply {
            put("tag", tag)
        }

        newDatabase.insert("hashtags", OnConflictStrategy.IGNORE, contentValues)
    }
}