package com.klinker.android.twitter_l.data.roomdb.transfers

import android.content.Context
import android.database.Cursor
import androidx.sqlite.db.SupportSQLiteDatabase
import com.klinker.android.twitter_l.data.sq_lite.FavoriteTweetsSQLiteHelper


class FavoriteTweetTransfer(context: Context) : TalonDatabaseCallback(context.getDatabasePath("favorite_tweets.db"), FavoriteTweetsSQLiteHelper.TABLE_FAVORITE_TWEETS, FavoriteTweetsSQLiteHelper.COLUMN_ID) {
    override fun onEachTableRow(cursor: Cursor, newDatabase: SupportSQLiteDatabase) {
        TODO("Not yet implemented")
    }

}