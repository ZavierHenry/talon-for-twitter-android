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
            "${UserTweetsSQLiteHelper.COLUMN_ID} integer primary key"
            //Implement rest of source column tweets
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