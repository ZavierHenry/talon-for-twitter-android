package transfertests

import android.content.ContentValues

import com.klinker.android.twitter_l.data.sq_lite.FollowersSQLiteHelper

import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher
import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

import java.util.concurrent.atomic.AtomicLong

class FollowersTransferTest : TransferTest() {

    private var userLabeler: AtomicLong? = null

    @Before
    fun resetUserLabeler() {
        if (userLabeler == null) {
            userLabeler = AtomicLong()
        }

        userLabeler!!.set(-2L)
    }


    @Test
    fun testBasicFollowersTransfer() {
        val contentValues = ContentValues()

        TransferTest.sourceDatabase!!.beginTransaction()

        for (i in 0..49) {
            val id = userLabeler!!.getAndDecrement()
            val account = 1
            val name = "Username $i"
            val screenName = "screen_name_$i"
            val profilePic = "https://img.twitter.com/image/$i.jpg"

        }

        TransferTest.sourceDatabase!!.setTransactionSuccessful()
        TransferTest.sourceDatabase!!.endTransaction()


    }


    @After
    fun clearDatabases() {
        clearSourceDatabase(FollowersSQLiteHelper.TABLE_HOME)
        clearTestDatabase()
    }

    companion object {

        @BeforeClass
        fun initDatabase() {
            val tableCreation = FollowersSQLiteHelper.DATABASE_CREATE

            TransferTest.initSourceDatabase(tableCreation)
            TransferTest.initTestDatabase()
        }


        @AfterClass
        fun closeDatabase() {
            TransferTest.closeSourceDatabase()
            TransferTest.closeTestDatabase()
        }
    }
}


internal class MockFollower(val account: Int, val user: MockUser) {

}


internal class MockFollowerMatcher private constructor(private val expected: MockFollower) : TypeSafeMatcher<MockFollower>() {

    override fun matchesSafely(item: MockFollower): Boolean {
        return false
    }

    override fun describeTo(description: Description) {
        description.appendText("All fields should be equal")
    }

    override fun describeMismatchSafely(item: MockFollower, description: Description) {

    }

    companion object {

        fun matchesFollower(expected: MockFollower): MockFollowerMatcher {
            return MockFollowerMatcher(expected)
        }
    }
}
