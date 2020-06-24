package com.klinker.android.twitter_l.transfertests

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.klinker.android.twitter_l.data.roomdb.transfers.DirectMessageTransfer
import com.klinker.android.twitter_l.data.sq_lite.DMSQLiteHelper
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class DirectMessageTransferDatabaseTest {

    private val sourceColumns = listOf(
            "${DMSQLiteHelper.COLUMN_ID} integer primary key",
            "${DMSQLiteHelper.COLUMN_ACCOUNT} integer",
            "${DMSQLiteHelper.COLUMN_TWEET_ID} integer",
            "${DMSQLiteHelper.COLUMN_TYPE} integer",
            "${DMSQLiteHelper.COLUMN_TEXT} text not null",
            "${DMSQLiteHelper.COLUMN_NAME} text",
            "${DMSQLiteHelper.COLUMN_PRO_PIC} text",
            "${DMSQLiteHelper.COLUMN_SCREEN_NAME} text",
            "${DMSQLiteHelper.COLUMN_TIME} integer",
            "${DMSQLiteHelper.COLUMN_URL} text",
            "${DMSQLiteHelper.COLUMN_PIC_URL} text",
            "${DMSQLiteHelper.COLUMN_HASHTAGS} text",
            "${DMSQLiteHelper.COLUMN_USERS} text",
            "${DMSQLiteHelper.COLUMN_RETWEETER} text",
            "${DMSQLiteHelper.COLUMN_EXTRA_ONE} text",
            "${DMSQLiteHelper.COLUMN_EXTRA_TWO} text",
            "${DMSQLiteHelper.COLUMN_EXTRA_THREE} text",
            "${DMSQLiteHelper.COLUMN_MEDIA_LENGTH} integer default -1"
    )

    @get:Rule val database = with(ApplicationProvider.getApplicationContext<Context>()) {
        TestTransferDatabase(
                DMSQLiteHelper.TABLE_DM,
                "direct_messages",
                DirectMessageTransfer(this),
                sourceColumns,
                DMSQLiteHelper.COLUMN_ID
        )
    }

    @Test
    @Throws(Exception::class)
    fun basicDirectMessageTransfer() {
        TODO("Not implemented as of yet")
    }

}