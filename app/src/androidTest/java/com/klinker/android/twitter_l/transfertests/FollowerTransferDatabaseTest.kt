package com.klinker.android.twitter_l.transfertests

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.klinker.android.twitter_l.data.roomdb.transfers.FollowerTransfer
import com.klinker.android.twitter_l.data.sq_lite.FollowersSQLiteHelper
import com.klinker.android.twitter_l.mockentities.MockFollower
import com.klinker.android.twitter_l.mockentities.MockTransferFollower
import com.klinker.android.twitter_l.mockentities.matchers.MockEntityMatcher.Companion.matchesMockEntity
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class FollowerTransferDatabaseTest {
    private val sourceColumns = listOf(
            "${FollowersSQLiteHelper.COLUMN_ID} integer primary key",
            "${FollowersSQLiteHelper.COLUMN_ACCOUNT} integer",
            "${FollowersSQLiteHelper.COLUMN_NAME} text",
            "${FollowersSQLiteHelper.COLUMN_PRO_PIC} text",
            "${FollowersSQLiteHelper.COLUMN_SCREEN_NAME} text"
    )


    @get:Rule val database = with(ApplicationProvider.getApplicationContext<Context>()) {
        TestTransferDatabase(
                FollowersSQLiteHelper.TABLE_HOME,
                "followers",
                FollowerTransfer(this),
                sourceColumns,
                FollowersSQLiteHelper.COLUMN_ID
        )
    }

    @Test
    @Throws(Exception::class)
    fun basicFollowerTransfer() {

        val mockFollower = MockTransferFollower(1)

        val oldId = database.insertIntoSQLiteDatabase(mockFollower)
        assertThat("Failed SQLite insertion", oldId, not(equalTo(-1L)))
        assertThat("Failed SQLite insertion", database.sourceSize, equalTo(1))

        database.buildDestinationDatabase()

        assertThat("Entity did not transfer into the new database", database.destSize, equalTo(1))
        val follower = database.queryFromTalonDatabase("SELECT * FROM followers LIMIT 1")!!.use { cursor ->
            cursor.moveToFirst()
            MockFollower(cursor)
        }

        val expected = mockFollower.copyId(follower.id).mockEntity
        assertThat("Entities are not the same", expected, matchesMockEntity(follower))

    }





}