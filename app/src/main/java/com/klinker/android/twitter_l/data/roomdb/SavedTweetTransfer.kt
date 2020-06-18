package com.klinker.android.twitter_l.data.roomdb

import android.content.Context
import android.database.Cursor
import androidx.sqlite.db.SupportSQLiteDatabase
import com.klinker.android.twitter_l.data.sq_lite.SavedTweetSQLiteHelper


class SavedTweetTransfer(context: Context) : TalonDatabaseCallback(context.getDatabasePath("saved_tweets.db"), SavedTweetSQLiteHelper.TABLE_HOME) {
    override fun onEachTableRow(cursor: Cursor, newDatabase: SupportSQLiteDatabase) {
        TODO("Not yet implemented")
    }
}