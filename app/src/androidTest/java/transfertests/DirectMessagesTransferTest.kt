package transfertests

import android.database.Cursor

import com.klinker.android.twitter_l.data.sq_lite.DMSQLiteHelper

import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher
import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

class DirectMessagesTransferTest : TransferTest() {


    @Test
    fun testDirectMessagesTransfer() {

        //populate source database
        //call callback onCreate
        //test direct message transfer results

        val testCursor = TransferTest.testDatabase!!.query("SELECT * FROM direct_messages", null)

    }

    @Test
    fun testTransferIfEmptyTable() {

    }

    @Test
    fun testTransferIfNoSourceDatabase() {

    }


    @After
    fun clearDatabases() {
        TransferTest.clearTestDatabase()
        TransferTest.clearSourceDatabase(DMSQLiteHelper.TABLE_DM)
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
