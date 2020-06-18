package com.klinker.android.twitter_l.data.roomdb.transfers

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import androidx.room.OnConflictStrategy
import androidx.sqlite.db.SupportSQLiteDatabase
import com.klinker.android.twitter_l.data.sq_lite.QueuedSQLiteHelper

class DraftTransfer(context: Context) : TalonDatabaseCallback(context.getDatabasePath("queued.db"), QueuedSQLiteHelper.TABLE_QUEUED) {

    override fun onEachTableRow(cursor: Cursor, newDatabase: SupportSQLiteDatabase) {
        val type = cursor.getInt(cursor.getColumnIndex(QueuedSQLiteHelper.COLUMN_TYPE))
        if (type == QueuedSQLiteHelper.TYPE_DRAFT) {
            val account = cursor.getInt(cursor.getColumnIndex(QueuedSQLiteHelper.COLUMN_ACCOUNT))
            val text = cursor.getString(cursor.getColumnIndex(QueuedSQLiteHelper.COLUMN_TEXT))
            val contentValues = ContentValues().apply {
                put("account", account)
                put("text", text)
            }

            newDatabase.insert("drafts", OnConflictStrategy.ABORT, contentValues)
        }
    }

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
    }
}