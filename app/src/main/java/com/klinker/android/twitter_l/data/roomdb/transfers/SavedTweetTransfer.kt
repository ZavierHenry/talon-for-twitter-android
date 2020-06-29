package com.klinker.android.twitter_l.data.roomdb.transfers

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import androidx.room.OnConflictStrategy
import androidx.sqlite.db.SupportSQLiteDatabase
import com.klinker.android.twitter_l.data.sq_lite.SavedTweetSQLiteHelper


class SavedTweetTransfer(context: Context) : TalonDatabaseCallback(context.getDatabasePath("saved_tweets.db"), SavedTweetSQLiteHelper.TABLE_HOME) {
    override fun onEachTableRow(cursor: Cursor, newDatabase: SupportSQLiteDatabase) {
        val text = cursor.getString(cursor.getColumnIndex(SavedTweetSQLiteHelper.COLUMN_TEXT))
        val tweetId = cursor.getLong(cursor.getColumnIndex(SavedTweetSQLiteHelper.COLUMN_TWEET_ID))
        val name = cursor.getString(cursor.getColumnIndex(SavedTweetSQLiteHelper.COLUMN_NAME))
        val screenName = cursor.getString(cursor.getColumnIndex(SavedTweetSQLiteHelper.COLUMN_SCREEN_NAME))
        val profilePic = cursor.getString(cursor.getColumnIndex(SavedTweetSQLiteHelper.COLUMN_PRO_PIC))
        val time = cursor.getLong(cursor.getColumnIndex(SavedTweetSQLiteHelper.COLUMN_TIME))
        val retweeter = cursor.getString(cursor.getColumnIndex(SavedTweetSQLiteHelper.COLUMN_RETWEETER))
        val picUrls = cursor.getString(cursor.getColumnIndex(SavedTweetSQLiteHelper.COLUMN_PIC_URL))
        val urls = cursor.getString(cursor.getColumnIndex(SavedTweetSQLiteHelper.COLUMN_URL))
        val users = cursor.getString(cursor.getColumnIndex(SavedTweetSQLiteHelper.COLUMN_USERS))
        val hashtags = cursor.getString(cursor.getColumnIndex(SavedTweetSQLiteHelper.COLUMN_HASHTAGS))
        val account = cursor.getInt(cursor.getColumnIndex(SavedTweetSQLiteHelper.COLUMN_ACCOUNT))

        val gifUrl = cursor.getString(cursor.getColumnIndex(SavedTweetSQLiteHelper.COLUMN_ANIMATED_GIF))
        val mediaLength = cursor.getLong(cursor.getColumnIndex(SavedTweetSQLiteHelper.COLUMN_MEDIA_LENGTH))


        val contentValues = ContentValues().apply {
            put("account", account)
            put("text", text)
            put("tweet_id", tweetId)
            put("user_name", name)
            put("user_screen_name", screenName)
            put("user_profile_pic", profilePic)

            put("time", time)
            if (!retweeter.isNullOrBlank()) {
                put("retweeter_screen_name", retweeter)
            }

            put("images", reserializeListString(picUrls, " "))
            put("urls", reserializeListString(urls, "  "))
            put("mentions", reserializeListString(users, "  "))
            put("hashtags", reserializeListString(hashtags, "  "))

            if (!gifUrl.isNullOrBlank()) {
                put("gif_url", gifUrl)
            }

            if (mediaLength != -1L) {
                put("media_length", mediaLength)
            }
        }

        newDatabase.insert("saved_tweets", OnConflictStrategy.IGNORE, contentValues)

    }
}