package transfertests


import android.content.ContentValues
import android.database.Cursor

import com.klinker.android.twitter_l.data.roomdb.TalonDatabase
import com.klinker.android.twitter_l.data.sq_lite.QueuedSQLiteHelper


import org.junit.After
import org.junit.AfterClass

import org.junit.BeforeClass
import org.junit.Test

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.not

import transfertests.MockQueuedTweetMatcher.Companion.matchesQueuedTweet
import transfertests.MockDraftMatcher.Companion.matchesDraft
import transfertests.MockScheduledTweetMatcher.Companion.matchesScheduledTweet
import java.util.*


class QueuedTransferTest : TransferTest() {

    @Test
    fun testBasicQueuedTransfer() {

        val draft = MockDraft(1, "This is sample text for the draft tweet")
        val scheduledTweet = MockScheduledTweet(1L, 2, "This is sample text for the scheduled tweet", 5000L)
        val queuedTweet = MockQueuedTweet(1, "This is sample text for the queued tweet")

        asTransaction {

            with(ContentValues()) {
                draft.setContentValues(this)
                val id = insertIntoSourceDatabase(QUEUED_TABLE_NAME, this)
                assertThat("Draft did not insert into the source database properly", id, not(-1L))
            }

            with(ContentValues()) {
                scheduledTweet.setContentValues(this)
                val id = insertIntoSourceDatabase(QUEUED_TABLE_NAME, this)
                assertThat("Scheduled tweet did not insert into the source database properly", id, not(-1L))
            }

            with(ContentValues()){
                queuedTweet.setContentValues(this)
                val id = insertIntoSourceDatabase(QUEUED_TABLE_NAME, this)
                assertThat("Queued tweet did not insert into the source database properly", id, not(-1L))
            }

        }

        applyCallback(TalonDatabase.transferQueuedData(context, sourceDatabasePath))

        queryTestDatabase("SELECT * FROM drafts", null).use { cursor ->
            assertThat("Draft did not transfer into the test database", cursor.count, `is`(1))
            assertThat("Problem moving drafts cursor to first draft", cursor.moveToFirst())
            val databaseDraft = MockDraft.create(cursor)
            assertThat("Source draft does not match transferred draft", databaseDraft, matchesDraft(draft))
        }

        queryTestDatabase("SELECT * FROM scheduled_tweets", null).use { cursor ->
            assertThat("Scheduled tweet did not transfer into the test database", cursor.count, `is`(1))
            assertThat("Problem moving scheduled tweet cursor to first value", cursor.moveToFirst())
            val databaseScheduledTweet = MockScheduledTweet.create(cursor)
            assertThat("Source scheduled tweet does not match transferred scheduled tweet", databaseScheduledTweet, matchesScheduledTweet(scheduledTweet))
        }

        queryTestDatabase("SELECT * FROM queued_tweets", null).use { cursor ->
            assertThat("Queued tweet did not transfer into the test database", cursor.count, `is`(1))
            assertThat("Problem moving queued tweet cursor to first value", cursor.moveToFirst())
            val databaseQueuedTweet = MockQueuedTweet.create(cursor)
            assertThat("Source queued tweet does not match transferred queued tweet", databaseQueuedTweet, matchesQueuedTweet(queuedTweet))
        }

    }

    @Test
    fun testTransferIfNoSourceTable() {
        applyCallback(TalonDatabase.transferQueuedData(context, sourceDatabasePath))

        queryTestDatabase("SELECT id FROM queued_tweets", null).use { cursor ->
            assertThat("Values are in queued tweets table that shouldn't be", cursor.count, `is`(0))
        }

        queryTestDatabase("SELECT alarm_id FROM scheduled_tweets", null).use { cursor ->
            assertThat("Values are in scheduled tweets that shoudn't be", cursor.count, `is`(0))
        }

        queryTestDatabase("SELECT id FROM drafts", null).use { cursor ->
            assertThat("Values are in drafts table that shouldn't be", cursor.count, `is`(0))
        }
    }

    @Test
    fun testTransferIfNoSourceDatabase() {
        applyCallback(TalonDatabase.transferQueuedData(context, TransferTest.badDatabaseLocation))
    }


    @After
    fun clearDatabases() {
        clearTestDatabase()
        clearSourceDatabase(QueuedSQLiteHelper.TABLE_QUEUED)
    }


    companion object {

        private const val QUEUED_TABLE_NAME = QueuedSQLiteHelper.TABLE_QUEUED


        @BeforeClass
        @JvmStatic fun initDatabase() {

            val tableCreation = QueuedSQLiteHelper.DATABASE_CREATE
            val createUniqueNameIndex = QueuedSQLiteHelper.ALTER_TABLE_ADD_UNIQUE

            TransferTest.initSourceDatabase(tableCreation, createUniqueNameIndex)
            TransferTest.initTestDatabase()
        }

        @AfterClass
        @JvmStatic fun closeDatabase() {
            TransferTest.closeSourceDatabase()
            TransferTest.closeTestDatabase()
        }
    }
}


internal class MockDraft(var account: Int?, var text: String?) : MockEntity<MockDraft>() {

    override fun setContentValues(contentValues: ContentValues) {
        with(contentValues) {
            put(QueuedSQLiteHelper.COLUMN_ACCOUNT, account)
            put(QueuedSQLiteHelper.COLUMN_TEXT, text)
            put(QueuedSQLiteHelper.COLUMN_TIME, 0L)
            put(QueuedSQLiteHelper.COLUMN_ALARM_ID, 0L)
            put(QueuedSQLiteHelper.COLUMN_TYPE, QueuedSQLiteHelper.TYPE_DRAFT)
        }

    }

    override fun showMismatches(other: MockDraft) : Collection<FieldMismatch> {

        val mismatches = ArrayList<FieldMismatch>()

        if (account != other.account) {
            mismatches.add(makeMismatch("account", account, other.account))
        }

        if (text != other.text) {
            mismatches.add(makeMismatch("text", text, other.text))
    }

        return mismatches
    }

    companion object {

        @JvmStatic fun create(cursor: Cursor) : MockDraft {

            return with(cursor) {
                val account = getInt(getColumnIndex("account"))
                val text = getString(getColumnIndex("text"))
                MockDraft(account, text)
            }
        }
    }
}

internal class MockScheduledTweet(var alarmId: Long?, var account: Int?, var text: String?, var time: Long?) : MockEntity<MockScheduledTweet>() {
    override fun showMismatches(other: MockScheduledTweet): Collection<FieldMismatch> {

        val mismatches = ArrayList<FieldMismatch>()

        if (alarmId != other.alarmId) {
            mismatches.add(makeMismatch("alarmId", alarmId, other.alarmId))
        }

        if (account != other.account) {
            mismatches.add(makeMismatch("account", account, other.account))
        }

        if (text != other.text) {
            mismatches.add(makeMismatch("text", text, other.text))
        }

        if (time != other.time) {
            mismatches.add(makeMismatch("time", time, other.time))
        }

        return mismatches
    }

    override fun setContentValues(contentValues: ContentValues) {
        with(contentValues) {
            put(QueuedSQLiteHelper.COLUMN_ALARM_ID, alarmId)
            put(QueuedSQLiteHelper.COLUMN_TYPE, QueuedSQLiteHelper.TYPE_SCHEDULED)
            put(QueuedSQLiteHelper.COLUMN_TIME, time)
            put(QueuedSQLiteHelper.COLUMN_ACCOUNT, account)
            put(QueuedSQLiteHelper.COLUMN_TEXT, text)
        }
    }

    companion object {

        @JvmStatic fun create(cursor: Cursor) : MockScheduledTweet {
            return with(cursor) {
                val alarmId = getLong(getColumnIndex("alarm_id"))
                val time = getLong(getColumnIndex("time"))
                val account = getInt(getColumnIndex("account"))
                val text = getString(getColumnIndex("text"))

                MockScheduledTweet(alarmId, account, text, time)
            }
        }

    }

}

internal class MockQueuedTweet(var account: Int?, var text: String?) : MockEntity<MockQueuedTweet>() {

    override fun showMismatches(other: MockQueuedTweet): Collection<FieldMismatch> {
        val mismatches = ArrayList<FieldMismatch>()

        if (account != other.account) {
            mismatches.add(makeMismatch("account", account, other.account))
        }

        if (text != other.text) {
            mismatches.add(makeMismatch("text", text, other.text))
        }

        return mismatches

    }

    override fun setContentValues(contentValues: ContentValues) {
        with(contentValues) {
            put(QueuedSQLiteHelper.COLUMN_TYPE, QueuedSQLiteHelper.TYPE_QUEUED_TWEET)
            put(QueuedSQLiteHelper.COLUMN_TEXT, text)
            put(QueuedSQLiteHelper.COLUMN_ACCOUNT, account)
            put(QueuedSQLiteHelper.COLUMN_TIME, 0L)
            put(QueuedSQLiteHelper.COLUMN_ALARM_ID, 0L)
        }
    }

    companion object {

        @JvmStatic fun create(cursor: Cursor) : MockQueuedTweet {
            return with(cursor) {
                val text = getString(getColumnIndex("text"))
                val account = getInt(getColumnIndex("account"))
                MockQueuedTweet(account, text)
            }
        }

    }

}


internal class MockDraftMatcher private constructor(expected: MockDraft) : MockMatcher<MockDraft>(expected) {

    companion object {

        @JvmStatic fun matchesDraft(expected: MockDraft): MockDraftMatcher {
            return MockDraftMatcher(expected)
        }
    }
}

internal class MockScheduledTweetMatcher private constructor(expected: MockScheduledTweet) : MockMatcher<MockScheduledTweet>(expected) {

    companion object {

        @JvmStatic fun matchesScheduledTweet(expected: MockScheduledTweet): MockScheduledTweetMatcher {
            return MockScheduledTweetMatcher(expected)
        }
    }


}

internal class MockQueuedTweetMatcher private constructor(expected: MockQueuedTweet) : MockMatcher<MockQueuedTweet>(expected) {


    companion object {

        @JvmStatic fun matchesQueuedTweet(expected: MockQueuedTweet): MockQueuedTweetMatcher {
            return MockQueuedTweetMatcher(expected)
        }
    }
}