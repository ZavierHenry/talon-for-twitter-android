package com.klinker.android.twitter_l.transfertests

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.klinker.android.twitter_l.data.roomdb.transfers.FavoriteUserNotificationTransfer
import com.klinker.android.twitter_l.data.sq_lite.FavoriteUserNotificationSQLiteHelper
import com.klinker.android.twitter_l.mockentities.MockFavoriteUserNotification
import com.klinker.android.twitter_l.mockentities.matchers.MockEntityMatcher.Companion.matchesMockEntity
import com.klinker.android.twitter_l.mockentities.transferentities.MockTransferFavoriteUserNotification
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class FavoriteUserNotificationTransferDatabaseTest {

    private val sourceColumns = listOf(
            "${FavoriteUserNotificationSQLiteHelper.COLUMN_ID} integer primary key",
            "${FavoriteUserNotificationSQLiteHelper.COLUMN_TWEET_ID} integer"
    )

    @get:Rule val database = with(ApplicationProvider.getApplicationContext<Context>()) {
        TestTransferDatabase(
                FavoriteUserNotificationSQLiteHelper.TABLE,
                "favorite_user_notifications",
                FavoriteUserNotificationTransfer(this),
                sourceColumns,
                FavoriteUserNotificationSQLiteHelper.COLUMN_ID
        )
    }

    @Test
    @Throws(Exception::class)
    fun testBasicFavoriteUserNotificationTransfer() {
        val mockNotification = MockTransferFavoriteUserNotification(3L)

        val oldId = database.insertIntoSQLiteDatabase(mockNotification)
        assertThat("Failed insertion into database", oldId, not(equalTo(-1L)))
        assertThat("Failed insertion into source SQLite database", database.sourceSize, equalTo(1))

        database.buildDestinationDatabase()

        assertThat("Entity did not transfer into the new database", database.destSize, equalTo(1))
        val favoriteUserNotification = database.queryFromTalonDatabase("SELECT * FROM favorite_user_notifications LIMIT 1")!!.use { cursor ->
            cursor.moveToFirst()
            MockFavoriteUserNotification(cursor)
        }

        val expected = mockNotification.copyId(favoriteUserNotification.id).mockEntity
        assertThat(expected, matchesMockEntity(favoriteUserNotification))

    }



}