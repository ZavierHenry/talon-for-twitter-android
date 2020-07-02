package com.klinker.android.twitter_l.data.roomdb.transfers

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import androidx.room.OnConflictStrategy
import androidx.sqlite.db.SupportSQLiteDatabase
import com.klinker.android.twitter_l.data.roomdb.ListStringConverter
import com.klinker.android.twitter_l.data.sq_lite.FavoriteTweetsSQLiteHelper


class FavoriteTweetTransfer(context: Context) : TalonDatabaseCallback(context.getDatabasePath("favorite_tweets.db"), FavoriteTweetsSQLiteHelper.TABLE_FAVORITE_TWEETS, FavoriteTweetsSQLiteHelper.COLUMN_ID) {
    override fun onEachTableRow(cursor: Cursor, newDatabase: SupportSQLiteDatabase) {
        val account = cursor.getInt(cursor.getColumnIndex(FavoriteTweetsSQLiteHelper.COLUMN_ACCOUNT))
        val text = cursor.getString(cursor.getColumnIndex(FavoriteTweetsSQLiteHelper.COLUMN_TEXT))
        val tweetId = cursor.getLong(cursor.getColumnIndex(FavoriteTweetsSQLiteHelper.COLUMN_TWEET_ID))
        val name = cursor.getString(cursor.getColumnIndex(FavoriteTweetsSQLiteHelper.COLUMN_NAME))
        val profilePic = cursor.getString(cursor.getColumnIndex(FavoriteTweetsSQLiteHelper.COLUMN_PRO_PIC))
        val screenName = cursor.getString(cursor.getColumnIndex(FavoriteTweetsSQLiteHelper.COLUMN_SCREEN_NAME))
        val time = cursor.getLong(cursor.getColumnIndex(FavoriteTweetsSQLiteHelper.COLUMN_TIME))
        val retweeter = cursor.getString(cursor.getColumnIndex(FavoriteTweetsSQLiteHelper.COLUMN_RETWEETER))
        val isUnread = cursor.getInt(cursor.getColumnIndex(FavoriteTweetsSQLiteHelper.COLUMN_UNREAD))
        val picUrls = cursor.getString(cursor.getColumnIndex(FavoriteTweetsSQLiteHelper.COLUMN_PIC_URL))
        val urls = cursor.getString(cursor.getColumnIndex(FavoriteTweetsSQLiteHelper.COLUMN_URL))
        val users = cursor.getString(cursor.getColumnIndex(FavoriteTweetsSQLiteHelper.COLUMN_USERS))
        val hashtags = cursor.getString(cursor.getColumnIndex(FavoriteTweetsSQLiteHelper.COLUMN_HASHTAGS))
        val source = cursor.getString(cursor.getColumnIndex(FavoriteTweetsSQLiteHelper.COLUMN_CLIENT_SOURCE))
        val conversation = cursor.getInt(cursor.getColumnIndex(FavoriteTweetsSQLiteHelper.COLUMN_CONVERSATION))

        val gifUrl = cursor.getString(cursor.getColumnIndex(FavoriteTweetsSQLiteHelper.COLUMN_ANIMATED_GIF))
        val mediaLength = cursor.getLong(cursor.getColumnIndex(FavoriteTweetsSQLiteHelper.COLUMN_MEDIA_LENGTH))

        val contentValues = ContentValues().apply {

            put("tweet_id", tweetId)
            put("user_screen_name", screenName)
            put("user_name", name)
            put("user_profile_pic", profilePic)

            put("text", text)
            put("time", time)

            put("images", reserializeListString(picUrls, " "))
            put("urls", reserializeListString(urls, "  "))
            put("mentions", reserializeListString(users, "  "))
            put("hashtags", reserializeListString(hashtags, "  "))

            put("liked", true)
            put("source", source)
            put("is_reply", conversation == 1)

            if (!gifUrl.isNullOrBlank()) {
                put("gif_url", gifUrl)
            }

            put("media_length", if (mediaLength != -1L) mediaLength else null)

            if (!retweeter.isNullOrBlank()) {
                put("retweeter_screen_name", retweeter)
            }

            put("account", account)
            put("is_unread", isUnread == 1)
        }

        newDatabase.insert("favorite_tweets", OnConflictStrategy.ABORT, contentValues)

    }
}