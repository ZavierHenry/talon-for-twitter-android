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


