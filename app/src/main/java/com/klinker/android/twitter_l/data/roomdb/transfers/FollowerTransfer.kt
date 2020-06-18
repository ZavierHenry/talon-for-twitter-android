package com.klinker.android.twitter_l.data.roomdb.transfers

import android.content.Context
import android.database.Cursor
import androidx.sqlite.db.SupportSQLiteDatabase
import com.klinker.android.twitter_l.data.sq_lite.FollowersSQLiteHelper


class FollowerTransfer(context: Context) : TalonDatabaseCallback(context.getDatabasePath("followers.db"), FollowersSQLiteHelper.TABLE_HOME) {
    override fun onEachTableRow(cursor: Cursor, newDatabase: SupportSQLiteDatabase) {
        TODO("Not yet implemented")
    }
}