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

class MentionDaoTest : DaoTest() {

    private val mentionDao get() = testDatabase.mentionDao()

    @Test
    fun insertMentionTest() {

    }

    @Test
    fun insertMentionWithConflict() {

    }

    @Test
    fun deleteMention() {

    }

    @Test
    fun getLatestScreenName() {

    }

    @Test
    fun getLatestUsers() {


    }


    @Test
    fun trimDatabaseTrimsDatabase() {

    }

    @Test
    fun trimDatabaseDoesNotTrimSmallDatabase() {

    }

    @Test
    fun mentionsCascadeDelete() {

    }


    private fun mockUser(): Array<String>? {
        return null
    }

    private fun mockTweet(): String? {
        return null
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
