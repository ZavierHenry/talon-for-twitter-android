package com.klinker.android.twitter_l.transfertests

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.klinker.android.twitter_l.data.roomdb.transfers.ListTweetTransfer
import com.klinker.android.twitter_l.data.sq_lite.ListSQLiteHelper
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class ListTweetTransferDatabaseTest {

    private val sourceColumns = listOf(
            "${ListSQLiteHelper.COLUMN_ID} integer primary key",
            "${ListSQLiteHelper.COLUMN_ACCOUNT} integer",
            "${ListSQLiteHelper.COLUMN_TWEET_ID} integer",
            "${ListSQLiteHelper.COLUMN_UNREAD} integer",
            "${ListSQLiteHelper.COLUMN_ARTICLE} text",
            "${ListSQLiteHelper.COLUMN_TEXT} text not null",
            "${ListSQLiteHelper.COLUMN_NAME} text",
            "${ListSQLiteHelper.COLUMN_PRO_PIC} text",
            "${ListSQLiteHelper.COLUMN_SCREEN_NAME} text",
            "${ListSQLiteHelper.COLUMN_TIME} integer",
            "${ListSQLiteHelper.COLUMN_URL} text",
            "${ListSQLiteHelper.COLUMN_PIC_URL} text",
            "${ListSQLiteHelper.COLUMN_HASHTAGS} text",
            "${ListSQLiteHelper.COLUMN_USERS} text",
            "${ListSQLiteHelper.COLUMN_RETWEETER} text",
            "${ListSQLiteHelper.COLUMN_LIST_ID} integer",
            "${ListSQLiteHelper.COLUMN_ANIMATED_GIF} text",
            "${ListSQLiteHelper.COLUMN_EXTRA_TWO} text",
            "${ListSQLiteHelper.COLUMN_EXTRA_THREE} text"
    )

    @get:Rule val database = with(ApplicationProvider.getApplicationContext<Context>()) {
        TestTransferDatabase(
                ListSQLiteHelper.TABLE_HOME,
                "list_tweets",
                ListTweetTransfer(this),
                sourceColumns,
                ListSQLiteHelper.COLUMN_ID
        )
    }

    @Test
    @Throws(Exception::class)
    fun testBasicListTweetTransfer() {
        TODO("Test not implemented yet")
    }

}