package transfertests

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log

import com.klinker.android.twitter_l.data.roomdb.TalonDatabase
import com.klinker.android.twitter_l.data.roomdb.entities.Hashtag
import com.klinker.android.twitter_l.data.sq_lite.HashtagSQLiteHelper

import org.hamcrest.Description
import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test


import java.util.ArrayList
import java.util.HashSet

import androidx.test.platform.app.InstrumentationRegistry

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.greaterThan
import org.hamcrest.Matchers.hasSize
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.isIn
import org.hamcrest.Matchers.notNullValue

class HashtagTransferTest : TransferTest() {

    @Test
    fun testBasicHashtagTransfer() {
        //fill table with data
        val contentValues = ContentValues()
        val tags = HashSet<String>()

        beginSourceDatabaseTransaction()


        (0..9).map{ i -> "#tag_$i"}.forEach {tag ->
            contentValues.put("name", tag)
            val id = insertIntoSourceDatabase(HashtagSQLiteHelper.TABLE_HASHTAGS, contentValues)
            if (id != -1L) {
                tags.add(tag)
            }
        }

        endSuccessfulSourceDatabaseTransaction()

        assertThat<Collection<String>>("Needs to have at least one tag to properly run this test", tags, hasSize(greaterThan(0)))


        applyCallback(TalonDatabase.transferHashtagData(sourceDatabasePath))

        val cursor = queryTestDatabase("SELECT name FROM hashtags", null)
        assertThat("Incorrect number of tags managed to transfer to the new database", cursor.count, `is`(tags.size))
        assertThat("Error getting the first value of the query", cursor.moveToFirst())

        do {

            val savedTag = cursor.getString(cursor.getColumnIndex("name"))
            assertThat("Tag was not saved properly", savedTag, isIn(tags))
            tags.remove(savedTag)

        } while (cursor.moveToNext())

        cursor.close()
    }

    @Test
    fun testTransferWithDuplicateNames() {
        val contentValues = ContentValues()
        val tags = HashSet<String>()

        beginSourceDatabaseTransaction()

        for (i in 0..20) {
            val tag = "#tag_" + i % 5
            contentValues.put(HashtagSQLiteHelper.COLUMN_TAG, tag)
            val id = insertIntoSourceDatabase(HashtagSQLiteHelper.TABLE_HASHTAGS, contentValues)
            if (id != -1L) {
                tags.add(tag)
            }
        }

        endSuccessfulSourceDatabaseTransaction()

        assertThat<Set<String>>("Needs to have at least one element to properly run this test", tags, hasSize(greaterThan(0)))
        applyCallback(TalonDatabase.transferHashtagData(TransferTest.sourceDatabase!!.path))

        val cursor = TransferTest.testDatabase!!.query("SELECT name FROM hashtags", null)
        assertThat("Incorrect number of tags transferred to the test database", cursor.count, `is`(tags.size))
        assertThat("Error moving to the first value of the cursor", cursor.moveToFirst())

        do {
            val savedTag = cursor.getString(cursor.getColumnIndex(HashtagSQLiteHelper.COLUMN_TAG))
            assertThat("Tag was not saved properly", savedTag, isIn(tags))

        } while (cursor.moveToNext())

        cursor.close()

    }

    @Test
    fun testTransferWithNullValues() {
        val contentValues = ContentValues()
        var nullValues = 0


        beginSourceDatabaseTransaction()

        for (i in 0..15) {
            val tag = if (i % 3 == 0) null else "#tag_$i"
            contentValues.put(HashtagSQLiteHelper.COLUMN_TAG, tag)
            val id = insertIntoSourceDatabase(HashtagSQLiteHelper.TABLE_HASHTAGS, contentValues)
            if (id != -1L && tag == null) {
                nullValues++
            }
        }

        endSuccessfulSourceDatabaseTransaction()

        assertThat("There needs to be at least one null value in the database to run this test properly", nullValues, greaterThan(0))
        applyCallback(TalonDatabase.transferHashtagData(sourceDatabasePath))
        val cursor = queryTestDatabase("SELECT name FROM hashtags WHERE name IS NULL", null)
        assertThat("Test database does not reject null values", cursor.count, `is`(0))
        cursor.close()

    }

    @Test
    fun testTransferIfEmptyTable() {

        //copy source database to prevent deletion error

        applyCallback(TalonDatabase.transferHashtagData(sourceDatabasePath))
        val cursor = queryTestDatabase("SELECT * FROM hashtags", null)
        assertThat("Something went wrong in the database transfer", cursor.count, `is`(0))
        cursor.close()

    }

    @Test
    fun testTransferIfNoSource() {
        applyCallback(TalonDatabase.transferHashtagData(TransferTest.badDatabaseLocation))
    }


    @After
    fun clearDatabases() {
        clearTestDatabase()
        clearSourceDatabase(HashtagSQLiteHelper.TABLE_HASHTAGS)
    }

    companion object {

        @BeforeClass
        @JvmStatic fun initDatabase() {
            val tableCreation = HashtagSQLiteHelper.DATABASE_CREATE

            TransferTest.initSourceDatabase(tableCreation)
            TransferTest.initTestDatabase()
        }

        @AfterClass
        @JvmStatic fun closeDatabase() {
            TransferTest.closeSourceDatabase()
            TransferTest.closeTestDatabase()
        }
    }

}


internal class MockHashtag(var name: String) {

    fun setContentValues(contentValues: ContentValues) {
        contentValues.put(HashtagSQLiteHelper.COLUMN_TAG, name)
    }

    fun readCursor(cursor: Cursor) {
        this.name = cursor.getString(cursor.getColumnIndex("name"))
    }

}


internal class MockHashtagMatcher private constructor(expected: MockHashtag) : MockMatcher<MockHashtag>(expected) {

    override fun matchesSafely(item: MockHashtag): Boolean {
        return expected.name.contentEquals(item.name)
    }

    override fun describeMismatchSafely(item: MockHashtag, mismatchDescription: Description) {

    }

    companion object {

        @JvmStatic fun matchesHashtag(expected: MockHashtag): MockHashtagMatcher {
            return MockHashtagMatcher(expected)
        }
    }
}
