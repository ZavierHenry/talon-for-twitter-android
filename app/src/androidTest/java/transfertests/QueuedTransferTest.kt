package transfertests


import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.os.strictmode.SqliteObjectLeakedViolation

import com.klinker.android.twitter_l.data.roomdb.TalonDatabase
import com.klinker.android.twitter_l.data.sq_lite.QueuedSQLiteHelper

import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher
import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

import java.io.File

import androidx.annotation.CallSuper
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry

class QueuedTransferTest : TransferTest() {

    @Test
    fun testBasicQueuedTransfer() {

    }


    @Test
    fun testTransferIfNoSourceTable() {

    }

    @Test
    fun testTransferIfNoSourceDatabase() {
        applyCallback(TalonDatabase.transferQueuedData(TransferTest.badDatabaseLocation))
    }


    @After
    fun clearDatabases() {
        clearTestDatabase()
        clearSourceDatabase(QueuedSQLiteHelper.TABLE_QUEUED)
    }

    companion object {

        @BeforeClass
        fun initDatabase() {
            TransferTest.initSourceDatabase()
            TransferTest.initTestDatabase()
        }

        @AfterClass
        fun closeDatabase() {
            TransferTest.closeSourceDatabase()
            TransferTest.closeTestDatabase()
        }
    }
}


internal class MockDraft(var account: Int, var text: String)

internal class MockScheduledTweet {
    var alarmId: Long = 0
    var text: String? = null
    var account: Int = 0
    var date: Long = 0

}


internal class MockQueuedTweet {
    var text: String? = null
    var account: Int = 0
}


internal class MockDraftMatcher private constructor(expected: MockDraft) : MockMatcher<MockDraft>(expected) {

    override fun matchesSafely(item: MockDraft): Boolean {
        return expected.account == item.account && expected.text == item.text
    }

    override fun describeMismatchSafely(item: MockDraft, mismatchDescription: Description) {

    }

    companion object {

        fun matchesDraft(expected: MockDraft): MockDraftMatcher {
            return MockDraftMatcher(expected)
        }
    }
}

internal class MockScheduledTweetMatcher private constructor(expected: MockScheduledTweet) : MockMatcher<MockScheduledTweet>(expected) {

    override fun matchesSafely(item: MockScheduledTweet): Boolean {
        return false
    }

    override fun describeMismatchSafely(item: MockScheduledTweet, mismatchDescription: Description) {

    }

    companion object {

        fun matchesScheduledTweet(expected: MockScheduledTweet): MockScheduledTweetMatcher {
            return MockScheduledTweetMatcher(expected)
        }
    }


}

internal class MockQueuedTweetMatcher private constructor(expected: MockQueuedTweet) : MockMatcher<MockQueuedTweet>(expected) {

    override fun matchesSafely(item: MockQueuedTweet): Boolean {
        return expected.account == item.account && expected.text == item.text
    }

    override fun describeMismatchSafely(item: MockQueuedTweet, mismatchDescription: Description) {

    }

    companion object {

        fun matchesQueuedTweet(expected: MockQueuedTweet): MockQueuedTweetMatcher {
            return MockQueuedTweetMatcher(expected)
        }
    }
}