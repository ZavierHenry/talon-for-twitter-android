package com.klinker.android.twitter_l.data.roomdb.transfers

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import androidx.room.OnConflictStrategy
import androidx.sqlite.db.SupportSQLiteDatabase
import com.klinker.android.twitter_l.data.sq_lite.MentionsSQLiteHelper

class MentionTransfer(context: Context) : TalonDatabaseCallback(context.getDatabasePath("mentions.db"), MentionsSQLiteHelper.TABLE_MENTIONS, MentionsSQLiteHelper.COLUMN_ID) {
    override fun onEachTableRow(cursor: Cursor, newDatabase: SupportSQLiteDatabase) {
        val account = cursor.getInt(cursor.getColumnIndex(MentionsSQLiteHelper.COLUMN_ACCOUNT))
        val text = cursor.getString(cursor.getColumnIndex(MentionsSQLiteHelper.COLUMN_TEXT))
        val tweetId = cursor.getLong(cursor.getColumnIndex(MentionsSQLiteHelper.COLUMN_TWEET_ID))
        val isUnread = cursor.getInt(cursor.getColumnIndex(MentionsSQLiteHelper.COLUMN_UNREAD))

        val contentValues = ContentValues().apply {
            put("account", account)
        }

        newDatabase.insert("mentions", OnConflictStrategy.ABORT, contentValues)


    }
}