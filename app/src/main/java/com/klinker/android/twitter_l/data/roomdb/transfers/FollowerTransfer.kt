package com.klinker.android.twitter_l.data.roomdb.transfers

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import androidx.room.OnConflictStrategy
import androidx.sqlite.db.SupportSQLiteDatabase
import com.klinker.android.twitter_l.data.sq_lite.FollowersSQLiteHelper


class FollowerTransfer(context: Context) : TalonDatabaseCallback(context.getDatabasePath("followers.db"), FollowersSQLiteHelper.TABLE_HOME) {
    override fun onEachTableRow(cursor: Cursor, newDatabase: SupportSQLiteDatabase) {
        val userId = cursor.getLong(cursor.getColumnIndex(FollowersSQLiteHelper.COLUMN_ID))
        val account = cursor.getInt(cursor.getColumnIndex(FollowersSQLiteHelper.COLUMN_ACCOUNT))
        val name = cursor.getString(cursor.getColumnIndex(FollowersSQLiteHelper.COLUMN_NAME))
        val screenName = cursor.getString(cursor.getColumnIndex(FollowersSQLiteHelper.COLUMN_SCREEN_NAME))
        val profilePic = cursor.getString(cursor.getColumnIndex(FollowersSQLiteHelper.COLUMN_PRO_PIC))

        val contentValues = ContentValues().apply {
            put("user_id", userId)
            put("account", account)
            put("name", name)
            put("screen_name", screenName)
            put("profile_pic", profilePic)
        }

        newDatabase.insert("followers", OnConflictStrategy.ABORT, contentValues)
    }
}