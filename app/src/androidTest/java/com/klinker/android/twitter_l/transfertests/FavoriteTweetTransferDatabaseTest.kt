package com.klinker.android.twitter_l.transfertests

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.klinker.android.twitter_l.data.roomdb.transfers.FavoriteTweetTransfer
import com.klinker.android.twitter_l.data.sq_lite.FavoriteTweetsSQLiteHelper
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class FavoriteTweetTransferDatabaseTest {

    private val sourceColumns = listOf(
            "${FavoriteTweetsSQLiteHelper.COLUMN_ID} integer primary key",
            "${FavoriteTweetsSQLiteHelper.COLUMN_ACCOUNT} integer",
            "${FavoriteTweetsSQLiteHelper.COLUMN_TWEET_ID} integer",
            "${FavoriteTweetsSQLiteHelper.COLUMN_UNREAD} integer",
            "${FavoriteTweetsSQLiteHelper.COLUMN_TYPE} integer",
            "${FavoriteTweetsSQLiteHelper.COLUMN_ARTICLE} text",
            "${FavoriteTweetsSQLiteHelper.COLUMN_TEXT} text not null",
            "${FavoriteTweetsSQLiteHelper.COLUMN_NAME} text",
            "${FavoriteTweetsSQLiteHelper.COLUMN_PRO_PIC} text",
            "${FavoriteTweetsSQLiteHelper.COLUMN_SCREEN_NAME} text",
            "${FavoriteTweetsSQLiteHelper.COLUMN_TIME} integer",
            "${FavoriteTweetsSQLiteHelper.COLUMN_URL} text",
            "${FavoriteTweetsSQLiteHelper.COLUMN_PIC_URL} text",
            "${FavoriteTweetsSQLiteHelper.COLUMN_HASHTAGS} text",
            "${FavoriteTweetsSQLiteHelper.COLUMN_USERS} text",
            "${FavoriteTweetsSQLiteHelper.COLUMN_RETWEETER} text",
            "${FavoriteTweetsSQLiteHelper.COLUMN_ANIMATED_GIF} text",
            "${FavoriteTweetsSQLiteHelper.COLUMN_EXTRA_TWO} text",
            "${FavoriteTweetsSQLiteHelper.COLUMN_CLIENT_SOURCE} text",
            "${FavoriteTweetsSQLiteHelper.COLUMN_CONVERSATION} integer default 0",
            "${FavoriteTweetsSQLiteHelper.COLUMN_MEDIA_LENGTH} integer default -1"
    )

    @get:Rule val database = with(ApplicationProvider.getApplicationContext<Context>()) {
        TestTransferDatabase(
                FavoriteTweetsSQLiteHelper.TABLE_FAVORITE_TWEETS,
                "favorite_tweets",
                FavoriteTweetTransfer(this),
                sourceColumns,
                FavoriteTweetsSQLiteHelper.COLUMN_ID
        )
    }

    @Test
    @Throws(Exception::class)
    fun basicFavoriteTweetsTransfer() {
        TODO("Not implemented as of yet")
    }



}