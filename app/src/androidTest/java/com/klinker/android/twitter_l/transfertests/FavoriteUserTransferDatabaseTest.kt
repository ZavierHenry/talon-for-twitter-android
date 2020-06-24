package com.klinker.android.twitter_l.transfertests

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.klinker.android.twitter_l.data.roomdb.transfers.FavoriteUserTransfer
import com.klinker.android.twitter_l.data.sq_lite.FavoriteUsersSQLiteHelper
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class FavoriteUserTransferDatabaseTest {

    private val sourceColumns = listOf(
            "${FavoriteUsersSQLiteHelper.COLUMN_ID} integer primary key",
            "${FavoriteUsersSQLiteHelper.COLUMN_ACCOUNT} integer",
            "${FavoriteUsersSQLiteHelper.COLUMN_NAME} text",
            "${FavoriteUsersSQLiteHelper.COLUMN_PRO_PIC} text",
            "${FavoriteUsersSQLiteHelper.COLUMN_SCREEN_NAME} text"
    )

    @get:Rule val database = with(ApplicationProvider.getApplicationContext<Context>()) {
        TestTransferDatabase(
                FavoriteUsersSQLiteHelper.TABLE_HOME,
                "favorite_users",
                FavoriteUserTransfer(this),
                sourceColumns,
                FavoriteUsersSQLiteHelper.COLUMN_ID
        )
    }

    @Test
    @Throws(Exception::class)
    fun basicFavoriteUserTransfer() {
        TODO("Not implemented as of yet")
    }

}