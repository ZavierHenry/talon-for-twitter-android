package com.klinker.android.twitter_l.data.roomdb.transfers

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import androidx.room.OnConflictStrategy
import androidx.sqlite.db.SupportSQLiteDatabase
import com.klinker.android.twitter_l.data.roomdb.transfers.TalonDatabaseCallback
import com.klinker.android.twitter_l.data.sq_lite.UserTweetsSQLiteHelper


class UserTweetTransfer(context: Context) : TalonDatabaseCallback(context.getDatabasePath("user_tweets.db"), UserTweetsSQLiteHelper.TABLE_HOME) {

    override fun onEachTableRow(cursor: Cursor, newDatabase: SupportSQLiteDatabase) {
        val text = cursor.getString(cursor.getColumnIndex(UserTweetsSQLiteHelper.COLUMN_TEXT))
        val tweetId = cursor.getLong(cursor.getColumnIndex(UserTweetsSQLiteHelper.COLUMN_TWEET_ID))
        val name = cursor.getString(cursor.getColumnIndex(UserTweetsSQLiteHelper.COLUMN_NAME))
        val profilePic = cursor.getString(cursor.getColumnIndex(UserTweetsSQLiteHelper.COLUMN_PRO_PIC))
        val screenName = cursor.getString(cursor.getColumnIndex(UserTweetsSQLiteHelper.COLUMN_SCREEN_NAME))
        val time = cursor.getLong(cursor.getColumnIndex(UserTweetsSQLiteHelper.COLUMN_TIME))
        val retweeter = cursor.getString(cursor.getColumnIndex(UserTweetsSQLiteHelper.COLUMN_RETWEETER))
        val picUrls = cursor.getString(cursor.getColumnIndex(UserTweetsSQLiteHelper.COLUMN_PIC_URL))
        val urls = cursor.getString(cursor.getColumnIndex(UserTweetsSQLiteHelper.COLUMN_URL))
        val users = cursor.getString(cursor.getColumnIndex(UserTweetsSQLiteHelper.COLUMN_USERS))
        val hashtags = cursor.getString(cursor.getColumnIndex(UserTweetsSQLiteHelper.COLUMN_HASHTAGS))

        val userId = cursor.getLong(cursor.getColumnIndex(UserTweetsSQLiteHelper.COLUMN_USER_ID))
        val gifUrl = cursor.getString(cursor.getColumnIndex(UserTweetsSQLiteHelper.COLUMN_ANIMATED_GIF))
        val mediaLength = cursor.getLong(cursor.getColumnIndex(UserTweetsSQLiteHelper.COLUMN_MEDIA_LENGTH))

        val contentValues = ContentValues().apply {
            put("text", text)
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

            put("account_user_id", userId)

            if (!gifUrl.isNullOrBlank()) {
                put("gif_url", gifUrl)
            }

            if (mediaLength != 1L) {
                put("media_length", mediaLength)
            }

        }

        newDatabase.insert("user_tweets", OnConflictStrategy.IGNORE, contentValues)
    }
}