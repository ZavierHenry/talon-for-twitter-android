package com.klinker.android.twitter_l.transfertests

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.klinker.android.twitter_l.data.roomdb.transfers.UserTweetTransfer
import com.klinker.android.twitter_l.data.sq_lite.UserTweetsSQLiteHelper
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class UserTweetTransferDatabaseTest {

    private val sourceColumns = listOf(
            "${UserTweetsSQLiteHelper.COLUMN_ID} integer primary key",
            "${UserTweetsSQLiteHelper.COLUMN_ACCOUNT} integer",
            "${UserTweetsSQLiteHelper.COLUMN_TWEET_ID} integer",
            "${UserTweetsSQLiteHelper.COLUMN_UNREAD} integer",
            "${UserTweetsSQLiteHelper.COLUMN_ARTICLE} text",
            "${UserTweetsSQLiteHelper.COLUMN_TEXT} text not null",
            "${UserTweetsSQLiteHelper.COLUMN_NAME} text",
            "${UserTweetsSQLiteHelper.COLUMN_PRO_PIC} text",
            "${UserTweetsSQLiteHelper.COLUMN_SCREEN_NAME} text",
            "${UserTweetsSQLiteHelper.COLUMN_TIME} integer",
            "${UserTweetsSQLiteHelper.COLUMN_URL} text",
            "${UserTweetsSQLiteHelper.COLUMN_PIC_URL} text",
            "${UserTweetsSQLiteHelper.COLUMN_HASHTAGS} text",
            "${UserTweetsSQLiteHelper.COLUMN_USERS} text",
            "${UserTweetsSQLiteHelper.COLUMN_RETWEETER} text",
            "${UserTweetsSQLiteHelper.COLUMN_USER_ID} integer",
            "${UserTweetsSQLiteHelper.COLUMN_ANIMATED_GIF} text",
            "${UserTweetsSQLiteHelper.COLUMN_EXTRA_TWO} text",
            "${UserTweetsSQLiteHelper.COLUMN_EXTRA_THREE} text",
            "${UserTweetsSQLiteHelper.COLUMN_MEDIA_LENGTH} integer default -1"
    )

    @get:Rule val database = with(ApplicationProvider.getApplicationContext<Context>()) {
        TestTransferDatabase(
                UserTweetsSQLiteHelper.TABLE_HOME,
                "user_tweets",
                UserTweetTransfer(this),
                sourceColumns,
                UserTweetsSQLiteHelper.COLUMN_ID
        )
    }

    @Test
    @Throws(Exception::class)
    fun testBasicUserTweetTransfer() {
        TODO("Not implemented as of yet")
    }

}