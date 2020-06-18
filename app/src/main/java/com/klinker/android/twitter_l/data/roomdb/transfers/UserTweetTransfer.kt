package com.klinker.android.twitter_l.data.roomdb.transfers

import android.content.Context
import android.database.Cursor
import androidx.sqlite.db.SupportSQLiteDatabase
import com.klinker.android.twitter_l.data.roomdb.transfers.TalonDatabaseCallback
import com.klinker.android.twitter_l.data.sq_lite.UserTweetsSQLiteHelper


class UserTweetTransfer(context: Context) : TalonDatabaseCallback(context.getDatabasePath("user_tweets.db"), UserTweetsSQLiteHelper.TABLE_HOME) {

    override fun onEachTableRow(cursor: Cursor, newDatabase: SupportSQLiteDatabase) {
        TODO("Not yet implemented")
    }
}