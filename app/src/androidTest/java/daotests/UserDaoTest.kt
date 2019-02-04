package daotests

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

import com.klinker.android.twitter_l.data.roomdb.TalonDatabase

import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.klinker.android.twitter_l.data.roomdb.daos.UserDao
import com.klinker.android.twitter_l.data.roomdb.entities.User
import org.hamcrest.MatcherAssert.assertThat
import org.junit.*

import daotests.UserMatcher.Companion.matchesUser
import org.hamcrest.Matchers.*


class UserDaoTest : DaoTest() {

    private val userDao: UserDao
        get() = testDatabase.userDao()

    @Test
    fun saveUser() {

        val user = User(null, 1, "User 1", "user1", "https://img.twitter.com/1/pic.png", true)

        val savedUser = userDao.saveUser(user)
        assertThat("Returned null value when it shouldn't have", savedUser, notNullValue())

        val cursor = queryDatabase("SELECT * FROM users", null)
        assertThat("Incorrect number of values in the database", cursor.count, `is`(1))
        assertThat("Problem pointing to the first value in the database", cursor.moveToFirst())

        val databaseUser = cursorToUser(cursor)
        assertThat("User did not save properly", databaseUser, matchesUser(savedUser!!))

        cursor.close()

    }

    @Test
    fun saveUserAlreadyInDatabase() {

        val user = User(null, 1, "User 1", "user1", "https://img.twitter.com/1/img.png", false)
        val id = with(ContentValues()) {
            put("twitter_id", user.twitterId)
            put("name", user.name)
            put("screen_name", user.screenName)
            put("profile_pic", user.profilePic)
            put("is_verified", user.isVerified)
            insertIntoDatabase("users", SQLiteDatabase.CONFLICT_ABORT, this)
        }

        assertThat("Test user must be in database to test this properly", id, not(-1L))

        val insertedUser = userDao.saveUser(user)

        assertThat("Could not get user already in the database", insertedUser, notNullValue())
        assertThat("User gotten from database is not user already in database", insertedUser, matchesUser(user.copy(id = id)))

        queryDatabase("SELECT * FROM users", null).use { cursor ->
            assertThat("Only one value is in the database", cursor.count, `is`(1))
        }

    }

    @Test
    fun saveUserFindsUserById() {

    }

    @Test
    fun saveUserSearchesOnlyByIdIfExists() {

    }


    @Test
    fun saveUserFindsUserByScreenName() {

    }

    @Test
    fun saveUserFindsUserByTwitterId() {

    }

    @Test
    fun saveUserUpdatesUserAlreadyInDatabase() {

    }


    @Test
    fun deletesUserById() {
        val contentValues = ContentValues()

        with(contentValues) {
            put("twitter_id", 1)
            put("name", "User 1")
            put("screen_name", "user1")
            put("profile_pic", "https://img.twitter.com/pic.png")
            put("is_verified", false)
        }

        val id = insertIntoDatabase("users", SQLiteDatabase.CONFLICT_ABORT, contentValues)
        assertThat("Test value did not properly save to database", id, not(-1L))

        userDao.deleteUserById(id)

        val cursor = queryDatabase("SELECT id FROM users", null)
        assertThat("Value did not delete from the database", cursor.count, `is`(0))
        cursor.close()

    }



    @After
    fun clearTables() {
        clearTestDatabase()
    }


    companion object {

        @BeforeClass
        @JvmStatic fun initDatabase() {
            DaoTest.initTestDatabase()
        }

        @AfterClass
        @JvmStatic fun closeDatabase() {
            DaoTest.closeTestDatabase()
        }
    }

}

internal class UserMatcher private constructor(expected: User) : EntityMatcher<User>(expected) {
    override fun getMismatches(actual: User): Map<String, Mismatch> {

        return mapOf(
                "id" to (expected.id to actual.id),
                "twitterId" to (expected.twitterId to actual.twitterId),
                "name" to (expected.name to actual.name),
                "screenName" to (expected.screenName to actual.screenName),
                "profilePic" to (expected.profilePic to actual.profilePic),
                "isVerifed" to (expected.isVerified to actual.isVerified)
        ).filterValues { (expected, actual) -> expected != actual }
    }

    companion object {
        @JvmStatic fun matchesUser(expected: User) : UserMatcher {
            return UserMatcher(expected)
        }
    }
}
