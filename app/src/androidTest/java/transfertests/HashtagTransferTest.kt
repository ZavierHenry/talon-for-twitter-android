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


import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import transfertests.MockHashtagMatcher.Companion.matchesHashtag



class HashtagTransferTest : TransferTest() {

    @Test
    fun testBasicHashtagTransfer() {
        //fill table with data
        val contentValues = ContentValues()
        val hashtags : List<MockHashtag> = List(2) { MockHashtag("#tag_$it") }

        beginSourceDatabaseTransaction()

        val idCount = hashtags.asSequence().map{ hashtag ->
            hashtag.setContentValues(contentValues)
            insertIntoSourceDatabase(HashtagSQLiteHelper.TABLE_HASHTAGS, contentValues)
        }.count { it != -1L }

        endSuccessfulSourceDatabaseTransaction()

        assertThat("At least one hashtag must be inserted to properly run this test", idCount, greaterThan(0))

        applyCallback(TalonDatabase.transferHashtagData(context, sourceDatabasePath))

        val cursor = queryTestDatabase("SELECT * FROM hashtags", null)
        assertThat("Incorrect number of tags managed to transfer to the new database", cursor.count, `is`(idCount))
        assertThat("Error getting the first value of the query", cursor.moveToFirst())

        val matchers = hashtags.map { matchesHashtag(it) }

        do {

            val testHashtag = MockHashtag.create(cursor)
            assertThat("Source hashtag did not transfer to the test database", testHashtag, anyOf(matchers))

        } while (cursor.moveToNext())

        cursor.close()
    }

    @Test
    fun testTransferWithDuplicateNames() {
        val contentValues = ContentValues()
        val hashtags = List(3) { MockHashtag("#sampletag")}

        beginSourceDatabaseTransaction()

        val idCount = hashtags.asSequence().map { hashtag ->
            hashtag.setContentValues(contentValues)
            insertIntoSourceDatabase(HashtagSQLiteHelper.TABLE_HASHTAGS, contentValues)
        }.count { it != -1L }

        endSuccessfulSourceDatabaseTransaction()

        assertThat("Needs to have at least one element to properly run this test", idCount, greaterThan(0))
        applyCallback(TalonDatabase.transferHashtagData(context, sourceDatabasePath))

        val cursor = queryTestDatabase("SELECT * FROM hashtags", null)

        assertThat("Incorrect number of tags transferred to the test database", cursor.count, `is`(1))
        assertThat("Error moving to the first value of the cursor", cursor.moveToFirst())

        val testHashtag = MockHashtag.create(cursor)
        assertThat("Transferred hashtag does not match source hashtag", testHashtag, matchesHashtag(hashtags[0]))
        cursor.close()

    }

    @Test
    fun testTransferWithNullValues() {
        val contentValues = ContentValues()
        val hashtags = List(3) { x -> if (x == 0) MockHashtag(null) else MockHashtag("#tag_$x")}

        beginSourceDatabaseTransaction()

        val ids = hashtags.map { hashtag ->
            hashtag.setContentValues(contentValues)
            insertIntoSourceDatabase(HashtagSQLiteHelper.TABLE_HASHTAGS, contentValues)
        }

        endSuccessfulSourceDatabaseTransaction()

        assertThat("The null mock hashtag must be properly inserted in the database to properly run this test", ids[0], not(-1L))
        applyCallback(TalonDatabase.transferHashtagData(context, sourceDatabasePath))

        val cursor = queryTestDatabase("SELECT * FROM hashtags WHERE name IS NULL", null)
        assertThat("Test database does not reject null values", cursor.count, `is`(0))
        cursor.close()

    }

    @Test
    fun testTransferIfEmptyTable() {

        //copy source database to prevent deletion error

        applyCallback(TalonDatabase.transferHashtagData(context, sourceDatabasePath))
        val cursor = queryTestDatabase("SELECT * FROM hashtags", null)
        assertThat("Something went wrong in the database transfer", cursor.count, `is`(0))
        cursor.close()

    }

    @Test
    fun testTransferIfNoSource() {
        applyCallback(TalonDatabase.transferHashtagData(context, TransferTest.badDatabaseLocation))
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


internal class MockHashtag(var name: String?) : MockEntity<MockHashtag>() {
    override fun showMismatches(other: MockHashtag): Collection<FieldMismatch> {
        val mismatches = ArrayList<FieldMismatch>()

        if (name != other.name) {
            mismatches.add(makeMismatch("name", name, other.name))
        }

        return mismatches
    }

    override fun setContentValues(contentValues: ContentValues) {
        contentValues.put(HashtagSQLiteHelper.COLUMN_TAG, name)
    }

    companion object {

        @JvmStatic fun create(cursor: Cursor) : MockHashtag {
            val name = cursor.getString(cursor.getColumnIndex("name"))
            return MockHashtag(name)
        }
    }

}

internal class MockHashtagMatcher private constructor(expected: MockHashtag) : MockMatcher<MockHashtag>(expected) {
    companion object {
        @JvmStatic fun matchesHashtag(expected: MockHashtag) : MockHashtagMatcher {
            return MockHashtagMatcher(expected)
        }
    }
}
