package com.klinker.android.twitter_l.transfertests

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.klinker.android.twitter_l.data.roomdb.transfers.SavedTweetTransfer
import com.klinker.android.twitter_l.data.sq_lite.SavedTweetSQLiteHelper
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class SavedTweetTransferDatabaseTest {

    private val sourceColumns = listOf(
            "${SavedTweetSQLiteHelper.COLUMN_ID} integer primary key",
            "${SavedTweetSQLiteHelper.COLUMN_ACCOUNT} integer",
            "${SavedTweetSQLiteHelper.COLUMN_TWEET_ID} integer",
            "${SavedTweetSQLiteHelper.COLUMN_UNREAD} integer",
            "${SavedTweetSQLiteHelper.COLUMN_ARTICLE} text",
            "${SavedTweetSQLiteHelper.COLUMN_TEXT} text not null",
            "${SavedTweetSQLiteHelper.COLUMN_NAME} text",
            "${SavedTweetSQLiteHelper.COLUMN_PRO_PIC} text",
            "${SavedTweetSQLiteHelper.COLUMN_SCREEN_NAME} text",
            "${SavedTweetSQLiteHelper.COLUMN_TIME} integer",
            "${SavedTweetSQLiteHelper.COLUMN_URL} text",
            "${SavedTweetSQLiteHelper.COLUMN_PIC_URL} text",
            "${SavedTweetSQLiteHelper.COLUMN_HASHTAGS} text",
            "${SavedTweetSQLiteHelper.COLUMN_USERS} text",
            "${SavedTweetSQLiteHelper.COLUMN_RETWEETER} text",
            "${SavedTweetSQLiteHelper.COLUMN_ANIMATED_GIF} text",
            "${SavedTweetSQLiteHelper.COLUMN_EXTRA_TWO} text",
            "${SavedTweetSQLiteHelper.COLUMN_EXTRA_THREE} text",
            "${SavedTweetSQLiteHelper.COLUMN_MEDIA_LENGTH} integer default -1"
    )

    @get:Rule val database = with(ApplicationProvider.getApplicationContext<Context>()) {
        TestTransferDatabase(
                SavedTweetSQLiteHelper.TABLE_HOME,
                "saved_tweets",
                SavedTweetTransfer(this),
                sourceColumns,
                SavedTweetSQLiteHelper.COLUMN_ID
        )
    }

    @Test
    @Throws(Exception::class)
    fun testBasicSavedTweetTransfer() {
        TODO("Not implemented as of yet")
    }
}