package daotests

import androidx.test.platform.app.InstrumentationRegistry

import com.klinker.android.twitter_l.data.roomdb.entities.Follower
import com.klinker.android.twitter_l.data.roomdb.entities.User
import com.klinker.android.twitter_l.data.roomdb.pojos.DisplayFollower
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`

import org.junit.After
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test

class FollowerDaoTest : DaoTest() {

    private val followerDao get() = testDatabase.followerDao()
    private val context get() = InstrumentationRegistry.getInstrumentation().targetContext


    @Test
    fun saveFollower() {
        val user = User(null, 1, "User 1", "user1", "https://img.twitter.com/1/pic.png", false)
        val follower = Follower(account = 1)
        val account = 1
        val extendedFollower = DisplayFollower(user = user)

        val insertedExtendedFollower = followerDao.saveFollower(context, extendedFollower, account)

        queryDatabase("SELECT * FROM followers", null).use { cursor ->
            assertThat("Follower info did not save to database", cursor.count, `is`(1))
        }

        queryDatabase("SELECT * FROM users", null).use { cursor ->
            assertThat("User info did not save to database", cursor.count, `is`(1))
        }


    }


    @Test
    fun deleteFollowerTest() {

        val user = User(null, 1, "User 1", "user1", "https://img.twitter.com/1/pic.png", false)


    }



    @Test
    fun userDeletionFailsWhenFollowerExists() {

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


