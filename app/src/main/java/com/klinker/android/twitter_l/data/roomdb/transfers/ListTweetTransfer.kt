package com.klinker.android.twitter_l.data.roomdb.transfers

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import androidx.room.OnConflictStrategy
import androidx.sqlite.db.SupportSQLiteDatabase
import com.klinker.android.twitter_l.data.sq_lite.ListSQLiteHelper

class ListTweetTransfer(context: Context) : TalonDatabaseCallback(context.getDatabasePath("lists.db"), ListSQLiteHelper.TABLE_HOME, ListSQLiteHelper.COLUMN_ID) {
    override fun onEachTableRow(cursor: Cursor, newDatabase: SupportSQLiteDatabase) {
        val account = cursor.getInt(cursor.getColumnIndex(ListSQLiteHelper.COLUMN_ACCOUNT))
        val tweetId = cursor.getLong(cursor.getColumnIndex(ListSQLiteHelper.COLUMN_TWEET_ID))
        val name = cursor.getString(cursor.getColumnIndex(ListSQLiteHelper.COLUMN_NAME))
        val profilePic = cursor.getString(cursor.getColumnIndex(ListSQLiteHelper.COLUMN_PRO_PIC))
        val screenName = cursor.getString(cursor.getColumnIndex(ListSQLiteHelper.COLUMN_SCREEN_NAME))
        val time = cursor.getLong(cursor.getColumnIndex(ListSQLiteHelper.COLUMN_TIME))
        val retweeter = cursor.getString(cursor.getColumnIndex(ListSQLiteHelper.COLUMN_RETWEETER))
        val picUrls = cursor.getString(cursor.getColumnIndex(ListSQLiteHelper.COLUMN_PIC_URL))
        val urls = cursor.getString(cursor.getColumnIndex(ListSQLiteHelper.COLUMN_URL))
        val users = cursor.getString(cursor.getColumnIndex(ListSQLiteHelper.COLUMN_USERS))
        val hashtags = cursor.getString(cursor.getColumnIndex(ListSQLiteHelper.COLUMN_HASHTAGS))
        val listId = cursor.getString(cursor.getColumnIndex(ListSQLiteHelper.COLUMN_LIST_ID))

        val gifUrl = cursor.getString(cursor.getColumnIndex(ListSQLiteHelper.COLUMN_ANIMATED_GIF))
        val mediaLength = cursor.getLong(cursor.getColumnIndex(ListSQLiteHelper.COLUMN_MEDIA_LENGTH))

        val contentValues = ContentValues().apply {
            put("account", account)
            put("tweet_id", tweetId)
            put("user_name", name)
            put("user_profile_pic", profilePic)
            put("user_screen_name", screenName)
            put("time", time)

            if (!retweeter.isNullOrBlank()) {
                put("retweeter_screen_name", retweeter)
            }

            put("images", reserializeListString(picUrls, " "))
            put("urls", reserializeListString(urls, "  "))
            put("mentions", reserializeListString(users, "  "))
            put("hashtags", reserializeListString(hashtags, "  "))

            put("list_id", listId)
            if (!gifUrl.isNullOrBlank()) {
                put("gif_url", gifUrl)
            }

            if (mediaLength != -1L) {
                put("media_length", mediaLength)
            }

        }

        newDatabase.insert("list_tweets", OnConflictStrategy.IGNORE, contentValues)
    }

}