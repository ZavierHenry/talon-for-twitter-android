package com.klinker.android.twitter_l.transfertests

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.klinker.android.twitter_l.data.roomdb.Mention
import com.klinker.android.twitter_l.data.roomdb.transfers.MentionTransfer
import com.klinker.android.twitter_l.data.sq_lite.MentionsSQLiteHelper
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class MentionTransferDatabaseTest  {

    private val sourceColumns = listOf(
            "${MentionsSQLiteHelper.COLUMN_ID} integer primary key",
            "${MentionsSQLiteHelper.COLUMN_TWEET_ID} integer",
            "${MentionsSQLiteHelper.COLUMN_UNREAD} integer",
            "${MentionsSQLiteHelper.COLUMN_ACCOUNT} integer",
            "${MentionsSQLiteHelper.COLUMN_TYPE} integer",
            "${MentionsSQLiteHelper.COLUMN_TEXT} text not null",
            "${MentionsSQLiteHelper.COLUMN_NAME} text",
            "${MentionsSQLiteHelper.COLUMN_PRO_PIC} text",
            "${MentionsSQLiteHelper.COLUMN_SCREEN_NAME} text",
            "${MentionsSQLiteHelper.COLUMN_TIME} integer",
            "${MentionsSQLiteHelper.COLUMN_URL} text",
            "${MentionsSQLiteHelper.COLUMN_PIC_URL} text",
            "${MentionsSQLiteHelper.COLUMN_HASHTAGS} text",
            "${MentionsSQLiteHelper.COLUMN_USERS} text",
            "${MentionsSQLiteHelper.COLUMN_RETWEETER} text",
            "${MentionsSQLiteHelper.COLUMN_ANIMATED_GIF} text",
            "${MentionsSQLiteHelper.COLUMN_EXTRA_TWO} text",
            "${MentionsSQLiteHelper.COLUMN_EXTRA_THREE} text"
    )

    @get:Rule val database = with(ApplicationProvider.getApplicationContext<Context>()) {
        TestTransferDatabase(
                MentionsSQLiteHelper.TABLE_MENTIONS,
                "mentions",
                MentionTransfer(this),
                sourceColumns,
                MentionsSQLiteHelper.COLUMN_ID
        )
    }

    @Test
    @Throws(Exception::class)
    fun basicMentionTransfer() {
        TODO("Not implemented as of yet")
    }

}