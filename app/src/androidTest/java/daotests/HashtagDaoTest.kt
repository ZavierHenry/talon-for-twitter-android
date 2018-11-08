package daotests

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase


import com.klinker.android.twitter_l.data.roomdb.entities.Hashtag

import org.hamcrest.Matchers.greaterThan
import org.hamcrest.Matchers.`is`
import org.hamcrest.MatcherAssert.assertThat


import org.junit.After
import org.junit.AfterClass
import org.junit.Assert
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test


class HashtagDaoTest : DaoTest() {

    private val hashtagDao get() = testDatabase.hashtagDao()

    @Test
    fun trimDatabaseTrimsDatabase() {
        val databaseLimit = 5
        val contentValues = ContentValues()
        var actualSize = 0


        beginTransaction()

        for (i in 0..15) {
            contentValues.put("name", "#tag$i")
            val id = insertIntoDatabase("hashtags", SQLiteDatabase.CONFLICT_IGNORE, contentValues)

            if (id != -1L) {
                actualSize++
            }
        }

        endSuccessfulTransaction()

        assertThat("Database setup for test failed", actualSize, greaterThan(databaseLimit))
        hashtagDao.trimDatabase(databaseLimit)
        val cursor = queryDatabase("SELECT * FROM hashtags", null)
        assertThat("Database is not being trimmed properly", cursor.count, `is`(databaseLimit))
        cursor.close()

    }

    //test that trimming is actually taking the id in consideration

    @Test
    fun noTrimDatabaseIfDatabaseTooSmall() {
        val databaseLimit = 50
        val contentValues = ContentValues()
        var actualSize = 0

        beginTransaction()

        for (i in 0..10) {
            contentValues.put("name", "#tag_$i")
            val id = insertIntoDatabase("hashtags", SQLiteDatabase.CONFLICT_IGNORE, contentValues)
            if (id != -1L) {
                actualSize++
            }
        }

        endSuccessfulTransaction()
        hashtagDao.trimDatabase(databaseLimit)

        val cursor = queryDatabase("SELECT * FROM hashtags;", null)
        assertThat("Trim database incorrectly trims database", cursor.count, `is`(actualSize))
    }


    @Test
    fun insertHashtag() {
        val hashtag = Hashtag("#wired25")
        hashtagDao.insertTag(hashtag)
        val cursor = queryDatabase("SELECT name FROM hashtags WHERE name = ?", arrayOf(hashtag.tag))
        assertThat("Incorrect number of arguments", cursor.count, `is`(0))
        assertThat("Error getting first value of database", cursor.moveToFirst())

        val name = cursor.getString(0)
        assertThat("Hashtag wasn't inserted properly", name, `is`(hashtag.tag))
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
        hashtagDao.insertTag(hashtag)
        val cursor = queryDatabase("SELECT name FROM hashtags WHERE name = ?", arrayOf(name))
        assertThat("Database incorrectly allows duplicate names", cursor.count, `is`(1))

        //test for duplicate
    }

    @After
    fun clearDatabase() {
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
