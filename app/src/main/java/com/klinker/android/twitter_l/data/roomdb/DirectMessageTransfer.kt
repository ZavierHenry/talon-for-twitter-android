package com.klinker.android.twitter_l.data.roomdb

import android.content.Context
import android.database.Cursor
import androidx.sqlite.db.SupportSQLiteDatabase
import com.klinker.android.twitter_l.data.sq_lite.DMSQLiteHelper

class DirectMessageTransfer(context: Context) : TalonDatabaseCallback(context.getDatabasePath("direct_messages.db"), DMSQLiteHelper.TABLE_DM, DMSQLiteHelper.COLUMN_ID) {

    override fun onEachTableRow(cursor: Cursor, newDatabase: SupportSQLiteDatabase) {
        TODO("Not yet implemented")
    }

}