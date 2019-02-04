package daotests

import android.content.Context

import com.klinker.android.twitter_l.data.roomdb.TalonDatabase

import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.klinker.android.twitter_l.data.roomdb.daos.UserTweetDao

class UserTweetDaoTest : DaoTest() {

    private val userTweetDao
        get() = testDatabase.userTweetDao()

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
