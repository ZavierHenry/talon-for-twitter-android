package daotests

import android.content.Context

import com.klinker.android.twitter_l.data.roomdb.TalonDatabase

import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry

class HomeTweetDaoTest : DaoTest() {

    private val homeTweetDao get() = testDatabase.homeTweetDao()

    @Test
    fun insertHomeTweet() {

    }

    @Test
    fun insertHomeTweetWithConflicts() {

    }


    //tests for filtering out tweets


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
