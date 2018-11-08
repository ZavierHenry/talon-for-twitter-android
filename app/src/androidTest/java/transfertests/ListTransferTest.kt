package transfertests

import com.klinker.android.twitter_l.data.sq_lite.ListSQLiteHelper

import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher
import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

class ListTransferTest : TransferTest() {


    @Test
    fun testBasicListTransfer() {

    }

    @Test
    fun testTransferIfNoSourceDatabase() {

    }


    @After
    fun clearDatabases() {
        clearTestDatabase()
        clearSourceDatabase(ListSQLiteHelper.TABLE_HOME)
    }

    companion object {

        @BeforeClass
        fun initDatabase() {
            val tableCreation = ListSQLiteHelper.DATABASE_CREATE
            val addMediaLengthField = ListSQLiteHelper.DATABASE_ADD_MEDIA_LENGTH_FIELD

            TransferTest.initSourceDatabase(tableCreation, addMediaLengthField)
            TransferTest.initTestDatabase()
        }

        @AfterClass
        fun closeDatabase() {
            TransferTest.closeTestDatabase()
            TransferTest.closeSourceDatabase()
        }
    }
}


internal class MockList


internal class MockListMatcher private constructor(private val expected: MockList) : TypeSafeMatcher<MockList>() {

    override fun matchesSafely(item: MockList): Boolean {
        return false
    }

    override fun describeTo(description: Description) {

    }

    override fun describeMismatchSafely(item: MockList, description: Description) {

    }

    companion object {

        fun matchesTwitterList(expected: MockList): MockListMatcher {
            return MockListMatcher(expected)
        }
    }

}
