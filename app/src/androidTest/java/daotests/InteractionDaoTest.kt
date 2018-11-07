package daotests

import android.content.ContentValues
import android.content.Context
import android.database.Cursor

import com.klinker.android.twitter_l.data.roomdb.TalonDatabase
import com.klinker.android.twitter_l.data.roomdb.daos.InteractionDao

import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.Test

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.greaterThan
import org.hamcrest.Matchers.`is`

class InteractionDaoTest : DaoTest() {

    @Test
    fun insertFollowerIntraction() {

    }

    @Test
    fun insertRetweetInteraction() {

    }

    @Test
    fun insertFavoriteInteraction() {

    }

    @Test
    fun insertFavoriteUserInteraction() {

    }

    @Test
    fun insertQuotedTweetInteraction() {

    }

    @Test
    fun deleteAllInteractions() {

    }

    @Test
    fun getUnreadCount() {

    }

    @Test
    fun markRead() {

    }

    @Test
    fun getUsers() {

    }

    @Test
    fun markAllRead() {

    }

    @Test
    fun trimDatabaseTrimsDatabase() {
        val trimSize = 60
        val account = 1
        var cursor: Cursor

        //load data into database

        for (i in 0..399) {

        }


        cursor = DaoTest.testDatabase!!.query("SELECT id FROM interactions WHERE account = ?", arrayOf(Integer.toString(account)))
        assertThat("Setup for loading data into database failed", cursor.count, greaterThan(trimSize))

        DaoTest.testDatabase!!.interactionDao().trimDatabase(account, trimSize)
        cursor = DaoTest.testDatabase!!.query("SELECT id FROM interactions WHERE account = ?", arrayOf(Integer.toString(account)))
        assertThat("Database did not trim to specified size", cursor.count, `is`(trimSize))


    }

    @Test
    fun trimDatabaseWithSmallDatabase() {

        val trimSize = 10000
        val account = 1

        //load data into database

        DaoTest.testDatabase!!.interactionDao().trimDatabase(account, trimSize)


    }


    @After
    fun clearTables() {
        DaoTest.clearTestDatabase()
    }

    companion object {

        @Before
        fun initDatabase() {
            DaoTest.initTestDatabase()
        }

        @AfterClass
        fun closeDatabase() {
            DaoTest.closeTestDatabase()
        }
    }
}
