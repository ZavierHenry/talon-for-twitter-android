package com.klinker.android.twitter_l.transfertests

import android.content.ContentValues
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.klinker.android.twitter_l.data.roomdb.transfers.DraftTransfer
import com.klinker.android.twitter_l.data.sq_lite.QueuedSQLiteHelper
import com.klinker.android.twitter_l.mockentities.MockDraft
import com.klinker.android.twitter_l.mockentities.MockTransferDraft
import com.klinker.android.twitter_l.mockentities.matchers.MockEntityMatcher.Companion.matchesMockEntity
import org.hamcrest.CoreMatchers.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class DraftTransferDatabaseTest {

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
                "drafts",
                DraftTransfer(this),
                sourceColumns,
                QueuedSQLiteHelper.COLUMN_ID
        )
    }

    @Test
    @Throws(Exception::class)
    fun basicDraftTransfer() {

        val mockDraft = MockTransferDraft(1, "Test draft")

        val oldId = database.insertIntoSQLiteDatabase(mockDraft)
        assertThat("Failed insertion into database", oldId, not(equalTo(-1L)))
        assertThat("Failed insertion into source SQLite database", database.sourceSize, equalTo(1))

        database.buildDestinationDatabase()

        assertThat("Entity did not transfer into the new database", database.destSize, equalTo(1))
        val draft = database.queryFromTalonDatabase("SELECT * FROM drafts LIMIT 1")!!.use { cursor ->
            cursor.moveToFirst()
            MockDraft(cursor)
        }

        val expected = mockDraft.copyId(draft.id).mockEntity
        assertThat("Entities are different", expected, matchesMockEntity(draft))
    }



}