package daotests

import android.content.ContentValues

import com.klinker.android.twitter_l.data.roomdb.TalonDatabase

import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

class FollowerDaoTest : DaoTest() {

    private val followerDao get() = testDatabase.followerDao()


    @Test
    fun insertFollower() {

    }


    @Test
    fun deleteFollowerTest() {

    }

    @Test
    fun deleteFollowerById() {
        val contentValues = ContentValues()

    }


    @After
    fun clearTables() {
        clearTestDatabase()
    }

    companion object {

        @BeforeClass
        fun initDatabase() {
            DaoTest.initTestDatabase()
        }

        @AfterClass
        fun closeDatabase() {
            DaoTest.closeTestDatabase()
        }
    }
}
