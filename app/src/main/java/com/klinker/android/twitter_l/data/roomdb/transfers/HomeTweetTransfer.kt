package com.klinker.android.twitter_l.data.roomdb.transfers

import android.content.Context
import android.database.Cursor
import androidx.sqlite.db.SupportSQLiteDatabase
import com.klinker.android.twitter_l.data.sq_lite.HomeSQLiteHelper


class HomeTweetTransfer(context: Context) : TalonDatabaseCallback(context.getDatabasePath("tweets.db"), HomeSQLiteHelper.TABLE_HOME, HomeSQLiteHelper.COLUMN_ID) {
    override fun onEachTableRow(cursor: Cursor, newDatabase: SupportSQLiteDatabase) {
        TODO("Not yet implemented")
    }
}