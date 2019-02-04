package transfertests

import android.app.Instrumentation
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

import com.klinker.android.twitter_l.data.roomdb.TalonDatabase
import com.klinker.android.twitter_l.data.roomdb.entities.SavedTweet
import com.klinker.android.twitter_l.data.sq_lite.SavedTweetSQLiteHelper

import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher
import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

import java.util.ArrayList
import java.util.HashMap
import java.util.concurrent.atomic.AtomicLong

import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import transfertests.MockTweetMatcher.Companion.matchesTweet

class SavedTweetsTransferTest : TransferTest() {

    private val userLabeler: AtomicLong = AtomicLong(-2L)

    @Before
    fun resetUserLabeler() {
        userLabeler.set(-2L)
    }

    @Test
    fun testBasicSavedTweetsTransfer() {

        val insertedSavedTweets = ArrayList<MockSavedTweet>()
        val insertedReferenceTweets = ArrayList<MockTweet>()
        val insertedReferenceUsers = ArrayList<MockUser>()

        val contentValues = ContentValues()

        TransferTest.sourceDatabase!!.beginTransaction()

        for (i in 0..4) {
            val name = "Username $i"
            val screenName = "screenname$i"
            val profilePic = "https://img.twimg.com/$i.jpg"

            val tweetId = 2000L + i
            val text = "This is a test tweet for user $i"
            val retweeter = ""
            val hashtags = "#wired25"

        }


        TransferTest.sourceDatabase!!.setTransactionSuccessful()
        TransferTest.sourceDatabase!!.endTransaction()


    }

    @Test
    fun testTransferIfEmptyTable() {

        applyCallback(TalonDatabase.transferSavedTweetsData(context, TransferTest.sourceDatabase!!.path, userLabeler))
        val cursor = TransferTest.testDatabase!!.query("SELECT * FROM saved_tweets", null)
        assertThat("Database somehow stored values", cursor.count, `is`(0))
        cursor.close()

    }

    @Test
    fun testTransferIfNoSourceDatabase() {
        applyCallback(TalonDatabase.transferSavedTweetsData(context, TransferTest.badDatabaseLocation, userLabeler))
    }

    @After
    fun clearDatabases() {
        clearTestDatabase()
        clearSourceDatabase(SavedTweetSQLiteHelper.TABLE_HOME)
    }

    companion object {

        @BeforeClass
        fun initDatabase() {
            val tableCreation = SavedTweetSQLiteHelper.DATABASE_CREATE
            val addMediaLengthField = SavedTweetSQLiteHelper.DATABASE_ADD_MEDIA_LENGTH_FIELD

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


internal class MockSavedTweet(var account: Int, var referenceTweet: MockTweet) {

    fun setContentValues(contentValues: ContentValues) {

    }

    fun readCursor(cursor: Cursor) {

    }
}


internal class MockSavedTweetsMatcher private constructor(private val expected: MockSavedTweet) : TypeSafeMatcher<MockSavedTweet>() {

    override fun matchesSafely(item: MockSavedTweet): Boolean {
        return expected.account == item.account && matchesTweet(expected.referenceTweet).matches(item.referenceTweet)
    }

    override fun describeTo(description: Description) {
        description.appendText("All fields to match")
    }

    override fun describeMismatchSafely(item: MockSavedTweet, description: Description) {

    }

    companion object {

        fun matchesSavedTweet(expected: MockSavedTweet): MockSavedTweetsMatcher {
            return MockSavedTweetsMatcher(expected)
        }
    }


}
