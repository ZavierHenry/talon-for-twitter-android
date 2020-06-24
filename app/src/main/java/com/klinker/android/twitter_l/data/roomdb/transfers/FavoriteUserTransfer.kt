package com.klinker.android.twitter_l.data.roomdb.transfers

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import androidx.room.OnConflictStrategy
import androidx.sqlite.db.SupportSQLiteDatabase
import com.klinker.android.twitter_l.data.sq_lite.FavoriteUsersSQLiteHelper


class FavoriteUserTransfer(context: Context) : TalonDatabaseCallback(context.getDatabasePath("favUsers.db"), FavoriteUsersSQLiteHelper.TABLE_HOME) {
    override fun onEachTableRow(cursor: Cursor, newDatabase: SupportSQLiteDatabase) {
        val userId = cursor.getLong(cursor.getColumnIndex(FavoriteUsersSQLiteHelper.COLUMN_ID))
        val account = cursor.getInt(cursor.getColumnIndex(FavoriteUsersSQLiteHelper.COLUMN_ACCOUNT))
        val name = cursor.getString(cursor.getColumnIndex(FavoriteUsersSQLiteHelper.COLUMN_NAME))
        val screenName = cursor.getString(cursor.getColumnIndex(FavoriteUsersSQLiteHelper.COLUMN_SCREEN_NAME))
        val profilePic = cursor.getString(cursor.getColumnIndex(FavoriteUsersSQLiteHelper.COLUMN_PRO_PIC))

        val contentValues = ContentValues().apply {
            put("user_id", userId)
            put("screen_name", screenName)
            put("account", account)
            put("name", name)
            put("profile_pic", profilePic)
        }

        newDatabase.insert("favorite_users", OnConflictStrategy.ABORT, contentValues)
    }

}