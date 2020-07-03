package com.klinker.android.twitter_l.transfertests

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.klinker.android.twitter_l.data.roomdb.transfers.ScheduledTweetTransfer
import com.klinker.android.twitter_l.data.sq_lite.QueuedSQLiteHelper
import com.klinker.android.twitter_l.mockentities.MockQueuedTweet
import com.klinker.android.twitter_l.mockentities.MockScheduledTweet
import com.klinker.android.twitter_l.mockentities.matchers.MockEntityMatcher.Companion.matchesMockEntity
import com.klinker.android.twitter_l.mockentities.transferentities.MockTransferDraft
import com.klinker.android.twitter_l.mockentities.transferentities.MockTransferQueuedTweet
import com.klinker.android.twitter_l.mockentities.transferentities.MockTransferScheduledTweet
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class ScheduledTweetTransferDatabaseTest {

    private val sourceColumns = listOf(
            "${QueuedSQLiteHelper.COLUMN_ID} integer primary key",
            "${QueuedSQLiteHelper.COLUMN_ACCOUNT} integer",
            "${QueuedSQLiteHelper.COLUMN_TEXT} text",
            "${QueuedSQLiteHelper.COLUMN_TIME} integer",
            "${QueuedSQLiteHelper.COLUMN_TYPE} integer",
            "${QueuedSQLiteHelper.COLUMN_ALARM_ID} integer"
    )

    @get:Rule val database = with(ApplicationProvider.getApplicationContext<Context>()) {
        TestTransferDatabase(
                QueuedSQLiteHelper.TABLE_QUEUED,
                "scheduled_tweets",
                ScheduledTweetTransfer(this),
                sourceColumns,
                QueuedSQLiteHelper.COLUMN_ID
        )
    }


    @Test
    @Throws(Exception::class)
    fun basicScheduledTweetTransfer() {
        val mockScheduledTweet = MockTransferScheduledTweet(1, "Test scheduled tweet", 643L, 4L)

        val oldId = database.insertIntoSQLiteDatabase(mockScheduledTweet)
        assertThat("Failed insertion into database", oldId, not(equalTo(-1L)))
        assertThat("Failed insertion into source SQLite database", database.sourceSize, equalTo(1))

        database.buildDestinationDatabase()

        assertThat("Entity did not transfer into new database", database.destSize, equalTo(1))
        val scheduledTweet = database.queryFromTalonDatabase("SELECT * FROM scheduled_tweets LIMIT 1")!!.use { cursor ->
            cursor.moveToFirst()
            MockScheduledTweet(cursor)
        }

        val expected = mockScheduledTweet.copyId(scheduledTweet.id).mockEntity
        assertThat("Entities are different", expected, matchesMockEntity(scheduledTweet))
    }


    @Test
    @Throws(Exception::class)
    fun testScheduledTweetTransfer_NoTransferOtherQueuedTweetTypes() {
        val mockDraft = MockTransferDraft(1, "Test draft")
        val mockQueuedTweet = MockTransferQueuedTweet(1, "Test queued tweet")

        val mockDraftId = database.insertIntoSQLiteDatabase(mockDraft)
        val mockQueuedTweetId = database.insertIntoSQLiteDatabase(mockQueuedTweet)

        assertThat("Failed draft insertion into database", mockDraftId, not(equalTo(-1L)))
        assertThat("Failed queued tweet insertion into database", mockQueuedTweetId, not(equalTo(-1L)))

        database.buildDestinationDatabase()

        assertThat("Entities transfered into table that shouldn't have", database.destSize, equalTo(0))

    }

}