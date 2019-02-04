package transfertests

import android.database.Cursor
import com.klinker.android.twitter_l.data.roomdb.TalonDatabase

import com.klinker.android.twitter_l.data.sq_lite.DMSQLiteHelper

import org.hamcrest.Description
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.TypeSafeMatcher
import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import java.util.concurrent.atomic.AtomicLong

class DirectMessagesTransferTest : TransferTest() {

    private val userLabeler : AtomicLong = AtomicLong(-2L)

    @Before
    fun resetUserLabeler() {
        userLabeler.set(-2L)
    }

    @Test
    fun testDirectMessagesTransfer() {


        //populate source database


        //test direct message transfer results

        applyCallback(TalonDatabase.transferDirectMessageData(context, sourceDatabasePath))

        queryTestDatabase("SELECT * FROM direct_messages", null).use { cursor ->

        }

    }

    @Test
    fun testTransferIfEmptyTable() {
        applyCallback(TalonDatabase.transferDirectMessageData(context, TransferTest.badDatabaseLocation))

        queryTestDatabase("SELECT * FROM direct_messages", null).use { cursor ->
            assertThat("Somehow the database is populated by values", cursor.count, `is`(0))
        }
    }

    @Test
    fun testTransferIfNoSourceDatabase() {
        applyCallback(TalonDatabase.transferDirectMessageData(context, TransferTest.badDatabaseLocation))
    }


    @After
    fun clearDatabases() {
        clearTestDatabase()
        clearSourceDatabase(DMSQLiteHelper.TABLE_DM)
    }

    companion object {

        @BeforeClass
        fun initDatabase() {
            val tableCreation = DMSQLiteHelper.DATABASE_CREATE
            val addMediaLengthField = DMSQLiteHelper.DATABASE_ADD_MEDIA_LENGTH_FIELD

            TransferTest.initSourceDatabase(tableCreation, addMediaLengthField)
            TransferTest.initTestDatabase()
        }

        @AfterClass
        fun closeDatabase() {
            TransferTest.closeSourceDatabase()
            TransferTest.closeTestDatabase()
        }
    }
}

internal class MockDirectMessage {

    var text: String? = null

}


internal class MockDirectMessageMatcher : TypeSafeMatcher<MockDirectMessage>() {


    override fun matchesSafely(item: MockDirectMessage): Boolean {
        return false
    }

    override fun describeTo(description: Description) {
        description.appendText("All direct message fields should be equal")
    }

    override fun describeMismatchSafely(item: MockDirectMessage, description: Description) {

    }

}
