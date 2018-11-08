package transfertests

import android.database.Cursor

import com.klinker.android.twitter_l.data.roomdb.TalonDatabase
import com.klinker.android.twitter_l.data.roomdb.entities.Mention
import com.klinker.android.twitter_l.data.sq_lite.MentionsSQLiteHelper

import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher
import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

import java.time.LocalDateTime
import java.util.Calendar
import java.util.concurrent.atomic.AtomicLong
import java.util.function.LongBinaryOperator

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.notNullValue


class MentionsTransferTest : TransferTest() {

    private var userLabeler: AtomicLong = AtomicLong(-2L)

    @Before
    fun resetLabeler() {
        userLabeler.set(-2L)
    }

    @Test
    fun testBasicMentionsTransfer() {

        TransferTest.sourceDatabase!!.beginTransaction()

        for (i in 0..99) {

        }

        TransferTest.sourceDatabase!!.setTransactionSuccessful()
        TransferTest.sourceDatabase!!.endTransaction()
        //save mention info

        applyCallback(TalonDatabase.transferMentionsData(TransferTest.sourceDatabase!!.path, userLabeler!!))
        val cursor = TransferTest.testDatabase!!.query("SELECT * FROM mentions", null)
        assertThat("Error getting database to test", cursor, notNullValue())

    }

    @Test
    fun testTransferIfEmptyTable() {

        applyCallback(TalonDatabase.transferMentionsData(TransferTest.sourceDatabase!!.path, userLabeler!!))
        val cursor = TransferTest.testDatabase!!.query("SELECT id FROM mentions", null)
        assertThat("Error getting the database result for testing", cursor, notNullValue())
        assertThat("Somehow entries got into the database", cursor.count, `is`(0))
        cursor.close()
    }

    @Test
    fun testTransferIfNoSourceDatabase() {
        applyCallback(TalonDatabase.transferMentionsData(TransferTest.badDatabaseLocation, userLabeler))
    }

    @After
    fun clearDatabases() {
        clearTestDatabase()
        clearSourceDatabase(MentionsSQLiteHelper.TABLE_MENTIONS)
    }

    companion object {

        @BeforeClass
        fun initDatabase() {
            val tableCreation = MentionsSQLiteHelper.DATABASE_CREATE
            val addConvoField = MentionsSQLiteHelper.DATABASE_ADD_CONVO_FIELD
            val addMediaLength = MentionsSQLiteHelper.DATABASE_ADD_MEDIA_LENGTH_FIELD

            TransferTest.initSourceDatabase(tableCreation, addConvoField, addMediaLength)
            TransferTest.initTestDatabase()
        }

        @AfterClass
        fun closeDatabase() {
            TransferTest.closeTestDatabase()
            TransferTest.closeSourceDatabase()
        }
    }

}


internal class MockMention(var account: Int, var referencedTweet: MockTweet)


internal class MockMentionMatcher : TypeSafeMatcher<MockMention>() {

    override fun matchesSafely(item: MockMention): Boolean {
        return false
    }

    override fun describeTo(description: Description) {

    }

    override fun describeMismatchSafely(item: MockMention, description: Description) {

    }

}
