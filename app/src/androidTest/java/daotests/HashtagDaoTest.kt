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
        val databaseSize = 5
        val contentValues = ContentValues()
        val trimSize = 3


        var actualSize = 0


        asTransaction {

            for (i in 0..15) {
                contentValues.put("name", "#tag$i")
                val id = insertIntoDatabase("hashtags", SQLiteDatabase.CONFLICT_IGNORE, contentValues)

                if (id != -1L) {
                    actualSize++
                }
            }

        }

        assertThat("Database setup for test failed", actualSize, greaterThan(trimSize))
        hashtagDao.trimDatabase(trimSize)

        queryDatabase("SELECT * FROM hashtags", null).use { cursor ->
            assertThat("Database is not being trimmed properly", cursor.count, `is`(trimSize))
        }

    }

    //test that trimming is actually taking the id in consideration

    @Test
    fun noTrimDatabaseIfDatabaseTooSmall() {
        val databaseLimit = 50
        val contentValues = ContentValues()
        var actualSize = 0

        asTransaction {

            for (i in 0..10) {
                contentValues.put("name", "#tag_$i")
                val id = insertIntoDatabase("hashtags", SQLiteDatabase.CONFLICT_IGNORE, contentValues)
                if (id != -1L) {
                    actualSize++
                }
            }

        }

        hashtagDao.trimDatabase(databaseLimit)

        queryDatabase("SELECT * FROM hashtags;", null).use { cursor ->
            assertThat("Trim database incorrectly trims database", cursor.count, `is`(actualSize))
        }

    }


    @Test
    fun insertHashtag() {
        val hashtag = Hashtag(null, "#wired25")
        hashtagDao.insertTag(hashtag)

        queryDatabase("SELECT name FROM hashtags WHERE name = ?", arrayOf(hashtag.tag)).use { cursor ->
            assertThat("Incorrect number of arguments", cursor.count, `is`(0))
            assertThat("Error getting first value of database", cursor.moveToFirst())

            val name = cursor.getString(0)
            assertThat("Hashtag wasn't inserted properly", name, `is`(hashtag.tag))
        }

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
        val hashtag = Hashtag(tag = name)

        hashtagDao.insertTag(hashtag)
        queryDatabase("SELECT name FROM hashtags WHERE name = ?", arrayOf(name)).use { cursor ->
            assertThat("Database incorrectly allows duplicate names", cursor.count, `is`(1))
        }


        //test for duplicate
    }

    @After
    fun clearDatabase() {
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
