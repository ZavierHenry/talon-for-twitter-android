package com.klinker.android.twitter_l.transfertests

import android.content.ContentValues
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.klinker.android.twitter_l.data.roomdb.transfers.DraftTransfer
import com.klinker.android.twitter_l.data.sq_lite.QueuedSQLiteHelper
import com.klinker.android.twitter_l.mockentities.MockDraft
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

        val sourceContentValues = ContentValues().apply {
            put(QueuedSQLiteHelper.COLUMN_ACCOUNT, 1)
            put(QueuedSQLiteHelper.COLUMN_TEXT, "Test draft")
            put(QueuedSQLiteHelper.COLUMN_TIME, 1L)
            put(QueuedSQLiteHelper.COLUMN_TYPE, QueuedSQLiteHelper.TYPE_DRAFT)
            put(QueuedSQLiteHelper.COLUMN_ALARM_ID, 1L)
        }

        val oldId = database.insertIntoSQLiteDatabase(sourceContentValues)
        assertThat("Failed insertion into database", oldId, notNullValue())
        assertThat("Failed insertion into source SQLite database", database.sourceSize, equalTo(1))

        database.buildDestinationDatabase()

        assertThat("Entity did not transfer into the new database", database.destSize, equalTo(1))
        val draft = database.queryFromTalonDatabase("SELECT * FROM drafts LIMIT 1")!!.use { cursor ->
            cursor.moveToFirst()
            MockDraft(cursor)
        }

        assertThat("Problem transferring draft account", draft.draft.account, equalTo(1))
        assertThat("Problem transferring draft text", draft.draft.text, equalTo("Test draft"))

    }



}