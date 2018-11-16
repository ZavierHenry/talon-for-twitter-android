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


class QueuedTransferTest : TransferTest() {

    @Test
    fun testBasicQueuedTransfer() {
        val draftsContentValues = ContentValues()
        val scheduledContentValues = ContentValues()
        val queuedContentValues = ContentValues()

        val draft = MockDraft(1, "This is sample text for the draft tweet")
        val scheduledTweet = MockScheduledTweet(1L, 2, "This is sample text for the scheduled tweet", 5000L)
        val queuedTweet = MockQueuedTweet(1, "This is sample text for the queued tweet")

        draft.setContentValues(draftsContentValues)
        scheduledTweet.setContentValues(scheduledContentValues)
        queuedTweet.setContentValues(queuedContentValues)

        beginSourceDatabaseTransaction()

        val draftId = insertIntoSourceDatabase(QUEUED_TABLE_NAME, draftsContentValues)
        val scheduledTweetId = insertIntoSourceDatabase(QUEUED_TABLE_NAME, scheduledContentValues)
        val queuedTweetId = insertIntoSourceDatabase(QUEUED_TABLE_NAME, queuedContentValues)

        endSuccessfulSourceDatabaseTransaction()

        assertThat("Draft did not insert into the source database properly", draftId, not(-1L))
        assertThat("Scheduled tweet did not insert into the source database properly", scheduledTweetId, not(-1L))
        assertThat("Queued tweet did not insert into the source database properly", queuedTweetId, not(-1L))

        applyCallback(TalonDatabase.transferQueuedData(sourceDatabasePath))

        val draftsCursor = queryTestDatabase("SELECT * FROM drafts", null)
        val scheduledCursor = queryTestDatabase("SELECT * FROM scheduled_tweets", null)
        val queuedCursor = queryTestDatabase("SELECT * FROM queued_tweets", null)

        assertThat("Draft did not transfer into the test database", draftsCursor.count, `is`(1))
        assertThat("Scheduled tweet did not transfer into the test database", scheduledCursor.count, `is`(1))
        assertThat("Queued tweet did not transfer into the test database", queuedCursor.count, `is`(1))
        assertThat("Problem moving drafts cursor to first draft", draftsCursor.moveToFirst())
        assertThat("Problem moving scheduled tweet cursor to first value", scheduledCursor.moveToFirst())
        assertThat("Problem moving queued tweet cursor to first value", queuedCursor.moveToFirst())

        val transferDraft = MockDraft.create(draftsCursor)
        val transferScheduledTweet = MockScheduledTweet.create(scheduledCursor)
        val transferQueuedTweet = MockQueuedTweet.create(queuedCursor)

        assertThat("Source draft does not match transferred draft", transferDraft, matchesDraft(draft))
        assertThat("Source scheduled tweet does not match transferred scheduled tweet", transferScheduledTweet, matchesScheduledTweet(scheduledTweet))
        assertThat("Source queued tweet does not match transferred queued tweet", transferQueuedTweet, matchesQueuedTweet(queuedTweet))

        draftsCursor.close()
        scheduledCursor.close()
        queuedCursor.close()


    }

    @Test
    fun testTransferIfNoSourceTable() {
        applyCallback(TalonDatabase.transferQueuedData(sourceDatabasePath))
        val queuedCursor = queryTestDatabase("SELECT id FROM queued_tweets", null)
        val scheduledCursor = queryTestDatabase("SELECT alarm_id FROM scheduled_tweets", null)
        val draftsCursor = queryTestDatabase("SELECT id FROM drafts", null)

        assertThat("Values are in queued tweets table that shouldn't be", queuedCursor.count, `is`(0))
        assertThat("Values are in scheduled tweets table that shouldn't be", scheduledCursor.count, `is`(0))
        assertThat("Values are in drafts table that shouldn't be", draftsCursor.count, `is`(0))

        queuedCursor.close()
        scheduledCursor.close()
        draftsCursor.close()
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
        contentValues.put(QueuedSQLiteHelper.COLUMN_ACCOUNT, account)
        contentValues.put(QueuedSQLiteHelper.COLUMN_TEXT, text)
        contentValues.put(QueuedSQLiteHelper.COLUMN_TIME, 0L)
        contentValues.put(QueuedSQLiteHelper.COLUMN_ALARM_ID, 0L)
        contentValues.put(QueuedSQLiteHelper.COLUMN_TYPE, QueuedSQLiteHelper.TYPE_DRAFT)
    }

    override fun showMismatches(other: MockDraft) : List<FieldMismatch> {

        val mismatches = ArrayList<FieldMismatch>()

        if (account != other.account) {
            mismatches.add(makeMismatch("account", account, other.account))
        }

        if (text != other.text) {
            mismatches.add(makeMismatch("text", text, other.text))
        }

        return mismatches.toList()
    }

    companion object {

        @JvmStatic fun create(cursor: Cursor) : MockDraft {
            val account = cursor.getInt(cursor.getColumnIndex("account"))
            val text = cursor.getString(cursor.getColumnIndex("text"))
            return MockDraft(account, text)
        }
    }
}

internal class MockScheduledTweet(var alarmId: Long?, var account: Int?, var text: String?, var time: Long?) : MockEntity<MockScheduledTweet>() {
    override fun showMismatches(other: MockScheduledTweet): List<FieldMismatch> {

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

        return mismatches.toList()
    }

    override fun setContentValues(contentValues: ContentValues) {
        contentValues.put(QueuedSQLiteHelper.COLUMN_ALARM_ID, alarmId)
        contentValues.put(QueuedSQLiteHelper.COLUMN_TYPE, QueuedSQLiteHelper.TYPE_SCHEDULED)
        contentValues.put(QueuedSQLiteHelper.COLUMN_TIME, time)
        contentValues.put(QueuedSQLiteHelper.COLUMN_TEXT, text)
        contentValues.put(QueuedSQLiteHelper.COLUMN_ACCOUNT, account)
    }

    companion object {

        @JvmStatic fun create(cursor: Cursor) : MockScheduledTweet {
            val alarmId = cursor.getLong(cursor.getColumnIndex("alarm_id"))
            val time = cursor.getLong(cursor.getColumnIndex("time"))
            val account = cursor.getInt(cursor.getColumnIndex("account"))
            val text = cursor.getString(cursor.getColumnIndex("text"))

            return MockScheduledTweet(alarmId, account, text, time)
        }

    }

}

internal class MockQueuedTweet(var account: Int?, var text: String?) : MockEntity<MockQueuedTweet>() {

    override fun showMismatches(other: MockQueuedTweet): List<FieldMismatch> {
        val mismatches = ArrayList<FieldMismatch>()

        if (account != other.account) {
            mismatches.add(makeMismatch("account", account, other.account))
        }

        if (text != other.text) {
            mismatches.add(makeMismatch("text", text, other.text))
        }

        return mismatches.toList()

    }

    override fun setContentValues(contentValues: ContentValues) {
        contentValues.put(QueuedSQLiteHelper.COLUMN_TEXT, text)
        contentValues.put(QueuedSQLiteHelper.COLUMN_ACCOUNT, account)
        contentValues.put(QueuedSQLiteHelper.COLUMN_TIME, 0L)
        contentValues.put(QueuedSQLiteHelper.COLUMN_ALARM_ID, 0L)
        contentValues.put(QueuedSQLiteHelper.COLUMN_TYPE, QueuedSQLiteHelper.TYPE_QUEUED_TWEET)
    }

    companion object {

        @JvmStatic fun create(cursor: Cursor) : MockQueuedTweet {
            val text = cursor.getString(cursor.getColumnIndex("text"))
            val account = cursor.getInt(cursor.getColumnIndex("account"))

            return MockQueuedTweet(account, text)
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