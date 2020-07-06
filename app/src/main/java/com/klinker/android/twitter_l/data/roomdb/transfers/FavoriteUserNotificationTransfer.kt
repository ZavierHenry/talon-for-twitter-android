package com.klinker.android.twitter_l.data.roomdb.transfers

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import androidx.room.OnConflictStrategy
import androidx.sqlite.db.SupportSQLiteDatabase
import com.klinker.android.twitter_l.data.sq_lite.FavoriteUserNotificationSQLiteHelper


class FavoriteUserNotificationTransfer(context: Context) : TalonDatabaseCallback(context.getDatabasePath("favorite_user_notifications.db"), FavoriteUserNotificationSQLiteHelper.TABLE, FavoriteUserNotificationSQLiteHelper.COLUMN_ID) {
    override fun onEachTableRow(cursor: Cursor, newDatabase: SupportSQLiteDatabase) {
        val tweetId = cursor.getLong(cursor.getColumnIndex(FavoriteUserNotificationSQLiteHelper.COLUMN_TWEET_ID))

        val contentValues = ContentValues().apply {
            put("tweet_id", tweetId)
        }

        newDatabase.insert("favorite_user_notifications", OnConflictStrategy.IGNORE, contentValues)
    }

}