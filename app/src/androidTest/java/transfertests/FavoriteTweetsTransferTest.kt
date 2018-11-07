package transfertests

import android.content.ContentValues
import android.database.Cursor

import com.klinker.android.twitter_l.data.roomdb.entities.FavoriteTweet
import com.klinker.android.twitter_l.data.sq_lite.FavoriteTweetsSQLiteHelper

import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher
import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test


class FavoriteTweetsTransferTest : TransferTest() {


    @Test
    fun testBasicFavoriteTweetsTransfer() {

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
        TransferTest.clearSourceDatabase(FavoriteTweetsSQLiteHelper.TABLE_FAVORITE_TWEETS)
    }

    companion object {

        @BeforeClass
        fun initDatabase() {
            val tableCreation = FavoriteTweetsSQLiteHelper.TABLE_FAVORITE_TWEETS
            val addConvField = FavoriteTweetsSQLiteHelper.DATABASE_ADD_CONVO_FIELD
            val addMediaLengthField = FavoriteTweetsSQLiteHelper.DATABASE_ADD_MEDIA_LENGTH_FIELD

            TransferTest.initSourceDatabase(tableCreation, addConvField, addMediaLengthField)
            TransferTest.initTestDatabase()
        }

        @AfterClass
        fun closeDatabase() {
            TransferTest.closeTestDatabase()
            TransferTest.closeSourceDatabase()
        }
    }
}

internal class MockFavoriteTweet {

    var account: Int = 0

    internal fun setContentValues(contentValues: ContentValues) {
        contentValues.put(FavoriteTweetsSQLiteHelper.COLUMN_ACCOUNT, account)
    }

    internal fun readCursor(cursor: Cursor) {
        this.account = cursor.getInt(cursor.getColumnIndex("account"))
    }
}

internal class MockFavoriteTweetMatcher private constructor(expected: MockFavoriteTweet) : MockMatcher<MockFavoriteTweet>(expected) {

    override fun matchesSafely(item: MockFavoriteTweet): Boolean {
        return false
    }


    override fun describeMismatchSafely(item: MockFavoriteTweet, description: Description) {

    }

    companion object {

        fun matchesFavoriteTweet(expected: MockFavoriteTweet): MockFavoriteTweetMatcher {
            return MockFavoriteTweetMatcher(expected)
        }
    }

}
