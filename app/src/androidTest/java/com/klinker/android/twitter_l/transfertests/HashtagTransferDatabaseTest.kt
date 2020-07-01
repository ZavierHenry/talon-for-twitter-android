package com.klinker.android.twitter_l.transfertests

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.klinker.android.twitter_l.data.roomdb.transfers.HashtagTransfer
import com.klinker.android.twitter_l.data.sq_lite.HashtagSQLiteHelper
import com.klinker.android.twitter_l.mockentities.MockHashtag
import com.klinker.android.twitter_l.mockentities.matchers.MockEntityMatcher.Companion.matchesMockEntity
import com.klinker.android.twitter_l.mockentities.transferentities.MockTransferDraft
import com.klinker.android.twitter_l.mockentities.transferentities.MockTransferHashtag
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class HashtagTransferDatabaseTest {

    private val sourceColumns = listOf(
            "${HashtagSQLiteHelper.COLUMN_ID} integer primary key",
            "${HashtagSQLiteHelper.COLUMN_TAG} text"
    )

    @get:Rule val database = with(ApplicationProvider.getApplicationContext<Context>()) {
        TestTransferDatabase(
                HashtagSQLiteHelper.TABLE_HASHTAGS,
                "hashtags",
                HashtagTransfer(this),
                sourceColumns,
                HashtagSQLiteHelper.COLUMN_ID
        )
    }

    @Test
    @Throws(Exception::class)
    fun testBasicHashtagTransfer() {
        val mockTransferHashtag = MockTransferHashtag("#demthrones")

        val oldId = database.insertIntoSQLiteDatabase(mockTransferHashtag)
        assertThat("Setup entity did not insert into source database", oldId, not(equalTo(-1L)))
        assertThat("Entity is not in SQLite database", database.sourceSize, equalTo(1))

        database.buildDestinationDatabase()

        assertThat("Entity did not transfer to new database", database.destSize, equalTo(1))
        val mockHashtag = database.queryFromTalonDatabase("SELECT * FROM hashtags LIMIT 1")!!.use { cursor ->
            cursor.moveToFirst()
            MockHashtag(cursor)
        }

        val expected = mockTransferHashtag.copyId(mockHashtag.id).mockEntity
        assertThat("Entity values are unexpectedly changed in the new database", expected, matchesMockEntity(mockHashtag))
    }

}