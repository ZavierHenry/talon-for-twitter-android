package transfertests

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.klinker.android.twitter_l.data.roomdb.TalonDatabase

import com.klinker.android.twitter_l.data.sq_lite.FollowersSQLiteHelper

import org.hamcrest.Description
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.hamcrest.TypeSafeMatcher
import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import transfertests.MockFollowerMatcher.Companion.matchesFollower

import java.util.concurrent.atomic.AtomicLong

class FollowersTransferTest : TransferTest() {


    @Test
    fun testBasicFollowersTransfer() {
        val followers = List(2) {
            MockFollower(2, MockUser(it.toLong(), "User $it", "user$it", "https://twitter.com/$it/img.png"))
        }

        val idCount = with(ContentValues()) {
            followers.asSequence().map { follower ->
                follower.setContentValues(this)
                insertIntoSourceDatabase(FollowersSQLiteHelper.TABLE_HOME, this)
            }.count { it != -1L }
        }

        assertThat("There must be at least 2 values in the database to test this properly", idCount, greaterThanOrEqualTo(2))

        queryTestDatabase("SELECT * FROM followers", null).use { cursor ->
            assertThat("Follower did not save to the database", cursor.count, `is`(idCount))
        }

        queryTestDatabase("SELECT * FROM users", null).use { cursor ->
            assertThat("User did not save to the database", cursor.count, `is`(idCount))
        }


        queryTestDatabase("SELECT account, twitter_id, name, screen_name, profile_pic, is_verified " +
                "FROM followers JOIN users ON followers.user_id = users.id", null).use { cursor ->

            assertThat("Follower id does not match with the user id", cursor.count, `is`(idCount))
            assertThat("Problem pointing to first value in database", cursor.moveToFirst())

            val matchers = followers.map { matchesFollower(it)}

            do {

                MockFollower.create(cursor).also { databaseFollower ->
                    assertThat("Follower/user is not properly saved in database", databaseFollower, anyOf(matchers))
                }

            } while (cursor.moveToNext())

        }

    }


    @Test
    fun transferTestWithPriorUser() {


        //apply followers callback


    }


    @Test
    fun transferTestWithPriorIncompleteUserInfo() {
        val follower = MockFollower(1, MockUser(1, "User 1", "user1", "https://img.twitter.com/img.png"))


        //apply followers callback


    }


    @Test
    fun transferTestWithCurrentIncompleteUserInfo() {


        //apply followers callback

    }


    @After
    fun clearDatabases() {
        clearSourceDatabase(FollowersSQLiteHelper.TABLE_HOME)
        clearTestDatabase()
    }

    companion object {

        @BeforeClass
        @JvmStatic fun initDatabase() {
            val tableCreation = FollowersSQLiteHelper.DATABASE_CREATE

            TransferTest.initSourceDatabase(tableCreation)
            TransferTest.initTestDatabase()
        }


        @AfterClass
        @JvmStatic fun closeDatabase() {
            TransferTest.closeSourceDatabase()
            TransferTest.closeTestDatabase()
        }
    }
}


internal class MockFollower(val account: Int, val user: MockUser) : MockEntity<MockFollower>() {
    override fun showMismatches(other: MockFollower): Collection<FieldMismatch> {

        return listOf(
                makeMismatch("account", account, other.account),
                makeMismatch("name", user.name, other.user.name),
                makeMismatch("screen_name", user.screenName, other.user.screenName),
                makeMismatch("profile_pic", user.profilePic, other.user.profilePic),
                makeMismatch("user_twitter_id", user.twitterId, other.user.twitterId),
                makeMismatch("is_verified", user.isVerified, other.user.isVerified)
        ).filterNot { (_, values) -> values.first == values.second }

    }

    override fun setContentValues(contentValues: ContentValues) {
        with(contentValues) {
            put(FollowersSQLiteHelper.COLUMN_ID, user.twitterId)
            put(FollowersSQLiteHelper.COLUMN_ACCOUNT, account)
            put(FollowersSQLiteHelper.COLUMN_NAME, user.name)
            put(FollowersSQLiteHelper.COLUMN_PRO_PIC, user.profilePic)
            put(FollowersSQLiteHelper.COLUMN_SCREEN_NAME, user.screenName)
        }
    }

    companion object {

        @JvmStatic fun create(cursor: Cursor) : MockFollower {
            return with(cursor) {
                val account = getInt(getColumnIndex("account"))
                val twitterId = getLong(getColumnIndex("twitter_id"))
                val name = getString(getColumnIndex("name"))
                val screenName = getString(getColumnIndex("screen_name"))
                val profilePic = getString(getColumnIndex("profile_pic"))
                val isVerified = getInt(getColumnIndex("is_verified")) == 1
                MockFollower(account, MockUser(twitterId, name, screenName, profilePic, isVerified))
            }
        }
    }

}


internal class MockFollowerMatcher private constructor(expected: MockFollower) : MockMatcher<MockFollower>(expected) {

    companion object {
        @JvmStatic fun matchesFollower(expected: MockFollower) : MockFollowerMatcher {
            return MockFollowerMatcher(expected)
        }
    }

}
