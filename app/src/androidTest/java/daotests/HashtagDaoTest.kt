package daotests

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase


import com.klinker.android.twitter_l.data.roomdb.entities.Hashtag
import com.klinker.android.twitter_l.data.roomdb.TalonDatabase

import org.hamcrest.Matchers.greaterThan
import org.hamcrest.Matchers.`is`
import org.hamcrest.MatcherAssert.assertThat


import org.junit.After
import org.junit.AfterClass
import org.junit.Assert
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.test.platform.app.InstrumentationRegistry


class HashtagDaoTest : DaoTest() {

    @Test
    fun trimDatabaseTrimsDatabase() {
        val databaseLimit = 5
        val contentValues = ContentValues()
        var actualSize = 0

        val openHelper = DaoTest.testDatabase!!.openHelper
        openHelper.writableDatabase.beginTransaction()

        for (i in 0..15) {
            contentValues.put("name", "#tag$i")
            val id = openHelper.writableDatabase.insert("hashtags", SQLiteDatabase.CONFLICT_IGNORE, contentValues)

            if (id != -1L) {
                actualSize++
            }
        }

        openHelper.writableDatabase.setTransactionSuccessful()
        openHelper.writableDatabase.endTransaction()

        assertThat("Database setup for test failed", actualSize, greaterThan(databaseLimit))

        DaoTest.testDatabase?.hashtagDao()!!.trimDatabase(databaseLimit)
        val cursor = DaoTest.testDatabase?.query("SELECT * FROM hashtags;", null)
        assertThat("Database is not being trimmed properly", cursor!!.count, `is`(databaseLimit))

    }

    //test that trimming is actually taking the id in consideration

    @Test
    fun noTrimDatabaseIfDatabaseTooSmall() {
        val databaseLimit = 50
        val contentValues = ContentValues()
        var actualSize = 0

        val openHelper = DaoTest.testDatabase!!.openHelper

        openHelper.writableDatabase.beginTransaction()

        for (i in 0..10) {
            contentValues.put("name", "#tag$i")
            val id = openHelper.writableDatabase.insert("hashtags", SQLiteDatabase.CONFLICT_IGNORE, contentValues)
            if (id != -1L) {
                actualSize++
            }
        }

        openHelper.writableDatabase.setTransactionSuccessful()
        openHelper.writableDatabase.endTransaction()


        DaoTest.testDatabase!!.hashtagDao().trimDatabase(databaseLimit)
        val cursor = DaoTest.testDatabase!!.query("SELECT * FROM hashtags;", null)
        assertThat("Trim database incorrectly trims database", cursor.count, `is`(actualSize))
    }


    @Test
    fun insertHashtag() {
        val hashtag = Hashtag("#wired25")
        DaoTest.testDatabase!!.hashtagDao().insertTag(hashtag)
        val cursor = DaoTest.testDatabase!!.query("SELECT name FROM hashtags WHERE name = ?", arrayOf("#wired25"))
        Assert.assertTrue(cursor.moveToFirst())

        val name = cursor.getString(0)
        assertThat("Hashtag wasn't inserted properly", name, `is`("#wired25"))
    }

    @Test
    fun getHashtagCursor() {
    }

    @Test
    fun getHashtagList() {
    }

    @Test
    fun hashtagTableRejectsDuplicates() {
        val name = "#tag3"
        val hashtag = Hashtag(name)
        DaoTest.testDatabase!!.hashtagDao().insertTag(hashtag)
        val cursor = DaoTest.testDatabase!!.query("SELECT name FROM hashtags WHERE name = ?", arrayOf(name))
        assertThat("Database incorrectly allows duplicate names", cursor.count, `is`(1))
    }

    @After
    fun clearDatabase() {
        DaoTest.testDatabase?.clearAllTables()
    }

    companion object {

        @BeforeClass
        fun initDatabase() {
            DaoTest.initTestDatabase()
        }

        @AfterClass
        fun closeDatabase() {
            DaoTest.testDatabase?.close()
            DaoTest.testDatabase = null
        }
    }

}
