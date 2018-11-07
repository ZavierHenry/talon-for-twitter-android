package transfertests

import android.content.ContentValues
import android.database.Cursor

import com.klinker.android.twitter_l.data.sq_lite.HomeSQLiteHelper

import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher
import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test


class HomeTweetsTransferTest : TransferTest() {


    @Test
    fun testBasicHomeTweetsTransfer() {

    }


    @After
    fun clearDatabases() {
        TransferTest.clearSourceDatabase(HomeSQLiteHelper.TABLE_HOME)
        TransferTest.clearTestDatabase()
    }

    companion object {

        @BeforeClass
        fun initDatabase() {
            val tableCreation = HomeSQLiteHelper.DATABASE_CREATE
            val addConvoField = HomeSQLiteHelper.DATABASE_ADD_CONVO_FIELD
            val addMediaLengthField = HomeSQLiteHelper.DATABASE_ADD_MEDIA_LENGTH_FIELD

            TransferTest.initSourceDatabase(tableCreation, addConvoField, addMediaLengthField)
            TransferTest.initTestDatabase()
        }

        @AfterClass
        fun closeDatabase() {
            TransferTest.closeSourceDatabase()
            TransferTest.closeTestDatabase()
        }
    }
}

internal class MockHomeTweet {

    var account: Int = 0
    var unread: Boolean = false
    var isCurrentPosition: Boolean = false
    var referencedTweet: MockTweet? = null

    internal fun setContentValues(contentValues: ContentValues) {
        contentValues.put(HomeSQLiteHelper.COLUMN_ACCOUNT, account)
    }

    internal fun readCursor(cursor: Cursor) {
        this.account = cursor.getInt(cursor.getColumnIndex("account"))
    }
}


internal class MockHomeTweetMatcher private constructor(expected: MockHomeTweet) : MockMatcher<MockHomeTweet>(expected) {

    override fun matchesSafely(item: MockHomeTweet): Boolean {
        return false
    }

    override fun describeMismatchSafely(item: MockHomeTweet, description: Description) {

    }

    companion object {

        fun matchesHomeTweet(expected: MockHomeTweet): MockHomeTweetMatcher {
            return MockHomeTweetMatcher(expected)
        }
    }

}
