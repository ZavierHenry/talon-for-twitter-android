package com.klinker.android.twitter_l.data.roomdb.transfers

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import androidx.room.OnConflictStrategy
import androidx.sqlite.db.SupportSQLiteDatabase
import com.klinker.android.twitter_l.data.roomdb.Mention
import com.klinker.android.twitter_l.data.sq_lite.MentionsSQLiteHelper

class MentionTransfer(context: Context) : TalonDatabaseCallback(context.getDatabasePath("mentions.db"), MentionsSQLiteHelper.TABLE_MENTIONS, MentionsSQLiteHelper.COLUMN_ID) {
    override fun onEachTableRow(cursor: Cursor, newDatabase: SupportSQLiteDatabase) {
        val account = cursor.getInt(cursor.getColumnIndex(MentionsSQLiteHelper.COLUMN_ACCOUNT))
        val text = cursor.getString(cursor.getColumnIndex(MentionsSQLiteHelper.COLUMN_TEXT))
        val tweetId = cursor.getLong(cursor.getColumnIndex(MentionsSQLiteHelper.COLUMN_TWEET_ID))
        val name = cursor.getString(cursor.getColumnIndex(MentionsSQLiteHelper.COLUMN_NAME))
        val profilePic = cursor.getString(cursor.getColumnIndex(MentionsSQLiteHelper.COLUMN_PRO_PIC))
        val screenName = cursor.getString(cursor.getColumnIndex(MentionsSQLiteHelper.COLUMN_SCREEN_NAME))
        val time = cursor.getLong(cursor.getColumnIndex(MentionsSQLiteHelper.COLUMN_TIME))
        val retweeter = cursor.getString(cursor.getColumnIndex(MentionsSQLiteHelper.COLUMN_RETWEETER))
        val isUnread = cursor.getInt(cursor.getColumnIndex(MentionsSQLiteHelper.COLUMN_UNREAD))
        val picUrls = cursor.getString(cursor.getColumnIndex(MentionsSQLiteHelper.COLUMN_PIC_URL))
        val urls = cursor.getString(cursor.getColumnIndex(MentionsSQLiteHelper.COLUMN_URL))
        val mentions = cursor.getString(cursor.getColumnIndex(MentionsSQLiteHelper.COLUMN_USERS))
        val hashtags = cursor.getString(cursor.getColumnIndex(MentionsSQLiteHelper.COLUMN_HASHTAGS))
        val isReply = cursor.getInt(cursor.getColumnIndex(MentionsSQLiteHelper.COLUMN_CONVERSATION))

        val gifUrl = cursor.getString(cursor.getColumnIndex(MentionsSQLiteHelper.COLUMN_ANIMATED_GIF))
        val mediaLength = cursor.getLong(cursor.getColumnIndex(MentionsSQLiteHelper.COLUMN_MEDIA_LENGTH))

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

            put("is_unread", isUnread)
            put("images", reserializeListString(picUrls, " "))
            put("urls", reserializeListString(urls, "  "))
            put("mentions", reserializeListString(mentions, "  "))
            put("hashtags", reserializeListString(hashtags, "  "))

            put("is_reply", isReply == 1)

            if (!gifUrl.isNullOrBlank()) {
                put("gif_url", gifUrl)
            }

            if (mediaLength != -1L) {
                put("media_length", mediaLength)
            }

        }

        newDatabase.insert("mentions", OnConflictStrategy.ABORT, contentValues)


    }
}