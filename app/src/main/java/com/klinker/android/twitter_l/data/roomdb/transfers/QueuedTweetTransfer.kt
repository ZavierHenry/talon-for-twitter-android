package com.klinker.android.twitter_l.data.roomdb.transfers

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import androidx.room.OnConflictStrategy
import androidx.sqlite.db.SupportSQLiteDatabase
import com.klinker.android.twitter_l.data.sq_lite.QueuedSQLiteHelper

class QueuedTweetTransfer(context: Context) : TalonDatabaseCallback(context.getDatabasePath("queued.db"), QueuedSQLiteHelper.TABLE_QUEUED) {
    override fun onEachTableRow(cursor: Cursor, newDatabase: SupportSQLiteDatabase) {
        val type = cursor.getInt(cursor.getColumnIndex(QueuedSQLiteHelper.COLUMN_TYPE))
        if (type == QueuedSQLiteHelper.TYPE_QUEUED_TWEET) {
            val text = cursor.getString(cursor.getColumnIndex(QueuedSQLiteHelper.COLUMN_TEXT))
            val account = cursor.getInt(cursor.getColumnIndex(QueuedSQLiteHelper.COLUMN_ACCOUNT))

            val contentValues = ContentValues().apply {
                put("text", text)
                put("account", account)
            }

            newDatabase.insert("queued_tweets", OnConflictStrategy.ABORT, contentValues)
        }
    }

}