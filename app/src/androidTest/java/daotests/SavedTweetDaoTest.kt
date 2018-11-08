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

class SavedTweetDaoTest : DaoTest() {

    private val savedTweetDao get() = testDatabase.savedTweetDao()

    @Test
    fun insertSavedTweet() {

    }

    @Test
    fun insertSavedTweetWithConflict() {

    }

    @Test
    fun insertSavedTweetSameIdDifferentAccount() {

    }

    @Test
    fun deleteSavedTweetObject() {

    }

    @Test
    fun deleteSavedTweetId() {

    }

    @Test
    fun getSavedTweetsCursor() {

    }

    @Test
    fun isTweetSaved() {

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
