package com.klinker.android.twitter_l.data.roomdb.transfers

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import androidx.room.OnConflictStrategy
import androidx.sqlite.db.SupportSQLiteDatabase
import com.klinker.android.twitter_l.data.sq_lite.DMSQLiteHelper

class DirectMessageTransfer(context: Context) : TalonDatabaseCallback(context.getDatabasePath("direct_messages.db"), DMSQLiteHelper.TABLE_DM, DMSQLiteHelper.COLUMN_ID) {

    override fun onEachTableRow(cursor: Cursor, newDatabase: SupportSQLiteDatabase) {
        val account = cursor.getInt(cursor.getColumnIndex(DMSQLiteHelper.COLUMN_ACCOUNT))
        val text = cursor.getString(cursor.getColumnIndex(DMSQLiteHelper.COLUMN_TEXT))
        val twitterId = cursor.getLong(cursor.getColumnIndex(DMSQLiteHelper.COLUMN_TWEET_ID))
        val senderName = cursor.getString(cursor.getColumnIndex(DMSQLiteHelper.COLUMN_NAME))
        val senderProfilePic = cursor.getString(cursor.getColumnIndex(DMSQLiteHelper.COLUMN_PRO_PIC))
        val senderScreenName = cursor.getString(cursor.getColumnIndex(DMSQLiteHelper.COLUMN_SCREEN_NAME))
        val time = cursor.getLong(cursor.getColumnIndex(DMSQLiteHelper.COLUMN_TIME))
        val recipientScreenName = cursor.getString(cursor.getColumnIndex(DMSQLiteHelper.COLUMN_RETWEETER))
        val recipientProfilePic = cursor.getString(cursor.getColumnIndex(DMSQLiteHelper.COLUMN_EXTRA_ONE))
        val recipientName = cursor.getString(cursor.getColumnIndex(DMSQLiteHelper.COLUMN_EXTRA_TWO))

        val gifUrl = cursor.getString(cursor.getColumnIndex(DMSQLiteHelper.COLUMN_EXTRA_THREE))
        val duration = cursor.getLong(cursor.getColumnIndex(DMSQLiteHelper.COLUMN_MEDIA_LENGTH))

        val contentValues = ContentValues().apply {
            put("twitter_id", twitterId)
            put("text", text)
            put("time", time)
            put("sender_name", senderName)
            put("sender_profile_pic", senderProfilePic)
            put("sender_screen_name", senderScreenName)
            put("recipient_screen_name", recipientScreenName)
            put("recipient_profile_pic", recipientProfilePic)
            put("recipient_name", recipientName)
            put("account", account)
        }

        newDatabase.insert("direct_messages", OnConflictStrategy.ABORT, contentValues)

    }

}