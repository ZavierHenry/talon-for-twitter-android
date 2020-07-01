package com.klinker.android.twitter_l.data.roomdb.transfers

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import androidx.room.OnConflictStrategy
import androidx.sqlite.db.SupportSQLiteDatabase
import com.klinker.android.twitter_l.activities.main_fragments.home_fragments.HomeExtensionFragment
import com.klinker.android.twitter_l.data.roomdb.ListStringConverter
import com.klinker.android.twitter_l.data.sq_lite.HomeSQLiteHelper


class HomeTweetTransfer(context: Context) : TalonDatabaseCallback(context.getDatabasePath("tweets.db"), HomeSQLiteHelper.TABLE_HOME, HomeSQLiteHelper.COLUMN_ID) {
    override fun onEachTableRow(cursor: Cursor, newDatabase: SupportSQLiteDatabase) {
        val account = cursor.getInt(cursor.getColumnIndex(HomeSQLiteHelper.COLUMN_ACCOUNT))
        val tweetId = cursor.getLong(cursor.getColumnIndex(HomeSQLiteHelper.COLUMN_TWEET_ID))
        val isUnread = cursor.getInt(cursor.getColumnIndex(HomeSQLiteHelper.COLUMN_UNREAD))
        val text = cursor.getString(cursor.getColumnIndex(HomeSQLiteHelper.COLUMN_TEXT))
        val name = cursor.getString(cursor.getColumnIndex(HomeSQLiteHelper.COLUMN_NAME))
        val profilePic = cursor.getString(cursor.getColumnIndex(HomeSQLiteHelper.COLUMN_PRO_PIC))
        val screenName = cursor.getString(cursor.getColumnIndex(HomeSQLiteHelper.COLUMN_SCREEN_NAME))
        val time = cursor.getLong(cursor.getColumnIndex(HomeSQLiteHelper.COLUMN_TIME))
        val picUrls = cursor.getString(cursor.getColumnIndex(HomeSQLiteHelper.COLUMN_PIC_URL))
        val urls = cursor.getString(cursor.getColumnIndex(HomeSQLiteHelper.COLUMN_URL))
        val retweeter = cursor.getString(cursor.getColumnIndex(HomeSQLiteHelper.COLUMN_RETWEETER))
        val hashtags = cursor.getString(cursor.getColumnIndex(HomeSQLiteHelper.COLUMN_HASHTAGS))
        val mentions = cursor.getString(cursor.getColumnIndex(HomeSQLiteHelper.COLUMN_USERS))
        val gifUrl = cursor.getString(cursor.getColumnIndex(HomeSQLiteHelper.COLUMN_ANIMATED_GIF))
        val isCurrentPosition = cursor.getString(cursor.getColumnIndex(HomeSQLiteHelper.COLUMN_CURRENT_POS))
        val source = cursor.getString(cursor.getColumnIndex(HomeSQLiteHelper.COLUMN_CLIENT_SOURCE))
        val isReply = cursor.getInt(cursor.getColumnIndex(HomeSQLiteHelper.COLUMN_CONVERSATION))
        val mediaLength = cursor.getLong(cursor.getColumnIndex(HomeSQLiteHelper.COLUMN_MEDIA_LENGTH))

        val contentValues = ContentValues().apply {
            put("account", account)
            put("tweet_id", tweetId)
            put("is_unread", isUnread == 1)
            put("text", text)

            put("user_name", name)
            put("user_profile_pic", profilePic)
            put("user_screen_name", screenName)

            put("time", time)
            put("images", reserializeListString(picUrls, " "))
            put("mentions", reserializeListString(mentions, "  "))
            put("urls", reserializeListString(urls, "  "))
            put("hashtags", reserializeListString(hashtags, "  "))

            if (!retweeter.isNullOrBlank()) {
                put("retweeter_screen_name", retweeter)
            }

            if (!gifUrl.isNullOrBlank()) {
                put("gif_url", gifUrl)
            }

            put("is_current_position", isCurrentPosition == "1")
            put("source", source)
            put("is_reply", isReply == 1)
            put("media_length", if (mediaLength != -1L) mediaLength else null)

        }

        newDatabase.insert("home_tweets", OnConflictStrategy.ABORT, contentValues)
    }
}