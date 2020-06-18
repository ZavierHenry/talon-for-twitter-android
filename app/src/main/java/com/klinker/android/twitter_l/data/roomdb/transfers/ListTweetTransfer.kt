package com.klinker.android.twitter_l.data.roomdb.transfers

import android.content.Context
import android.database.Cursor
import androidx.sqlite.db.SupportSQLiteDatabase
import com.klinker.android.twitter_l.data.sq_lite.ListSQLiteHelper

class ListTweetTransfer(context: Context) : TalonDatabaseCallback(context.getDatabasePath("lists.db"), ListSQLiteHelper.TABLE_HOME, ListSQLiteHelper.COLUMN_ID) {
    override fun onEachTableRow(cursor: Cursor, newDatabase: SupportSQLiteDatabase) {
        TODO("Not yet implemented")
    }

}