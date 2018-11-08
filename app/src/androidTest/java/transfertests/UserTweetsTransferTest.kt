package transfertests

import android.content.Context
import android.database.sqlite.SQLiteDatabase

import com.klinker.android.twitter_l.data.roomdb.TalonDatabase
import com.klinker.android.twitter_l.data.sq_lite.UserTweetsSQLiteHelper

import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher
import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

import java.io.File

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry


class UserTweetsTransferTest : TransferTest() {

    @Test
    fun testBasicUserTweetsTransfer() {

    }

    @Test
    fun testTransferIfNoSourceDatabase() {

    }


    @After
    fun clearDatabases() {
        clearSourceDatabase(UserTweetsSQLiteHelper.TABLE_HOME)
        clearTestDatabase()
    }

    companion object {

        @BeforeClass
        fun initDatabase() {
            TransferTest.initSourceDatabase()
            TransferTest.initTestDatabase()
        }

        @AfterClass
        fun closeDatabase() {
            TransferTest.closeTestDatabase()
            TransferTest.closeSourceDatabase()

        }
    }
}

internal class MockUserTweet {

    var account: Int = 0
    var referenceTweet: MockTweet? = null

}


internal class MockUserTweetMatcher private constructor(private val expected: MockUserTweet) : TypeSafeMatcher<MockUserTweet>() {

    override fun matchesSafely(item: MockUserTweet): Boolean {
        return false
    }

    override fun describeTo(description: Description) {

    }

    override fun describeMismatchSafely(item: MockUserTweet, description: Description) {

    }

    companion object {

        fun matchesUserTweet(expected: MockUserTweet): MockUserTweetMatcher {
            return MockUserTweetMatcher(expected)
        }
    }
}
