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
        val callback = TalonDatabase.transferHashtagData(context, sourceDatabasePath)
        testBasicTransfer(callback, sourceTableName, destTableName, { MockHashtag("tag_$it")}, { matchesHashtag(it) }, { MockHashtag.create(it) })
    }

    @Test
    fun testTransferWithDuplicateNames() {
        val contentValues = ContentValues()
        val hashtags = List(3) { MockHashtag("#sampletag")}

        val idCount = asTransaction {
            hashtags.count { hashtag ->
                insertIntoSourceDatabase(HashtagSQLiteHelper.TABLE_HASHTAGS, ContentValues().apply { hashtag.setContentValues(this) }) != -1L
            }
        }

        assertThat("Needs to have at least one element to properly run this test", idCount, greaterThan(0))
        applyCallback(TalonDatabase.transferHashtagData(context, sourceDatabasePath))

        val cursor = queryTestDatabase("SELECT * FROM hashtags", null).use { cursor ->
            assertThat("Incorrect number of tags transferred to the test database", cursor.count, `is`(1))
            assertThat("Error moving to the first value of the cursor", cursor.moveToFirst())
            assertThat("Transferred hashtag does not match source hashtag", MockHashtag.create(cursor), matchesHashtag(hashtags[0]))
        }

    }

    @Test
    fun testTransferWithNullValues() {
        val hashtags = List(3) { x -> if (x == 0) MockHashtag(null) else MockHashtag("#tag_$x")}

        val ids = asTransaction {
            hashtags.map { hashtag ->
                insertIntoSourceDatabase(HashtagSQLiteHelper.TABLE_HASHTAGS, ContentValues().apply { hashtag.setContentValues(this) })
            }
        }

        //assertThat("The null mock hashtag must be properly inserted in the database to properly run this test", ids[0], not(-1L))

        assertThat("The null mock hashtag must be properly inserted in the database to properly run this test", ids, contains(not(-1L), anything(), anything()))
        applyCallback(TalonDatabase.transferHashtagData(context, sourceDatabasePath))

        queryTestDatabase("SELECT 1 FROM hashtags WHERE name IS NULL", null).use { cursor ->
            assertThat("Test databaase does not reject null values", cursor.count, `is`(0))
        }

    }

    @Test
    fun testHashtagTransferIfEmptyTable() {

        //copy source database to prevent deletion error
        testTransferIfEmptyTable(TalonDatabase.transferHashtagData(context, sourceDatabasePath), destTableName)
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

        const val sourceTableName = HashtagSQLiteHelper.TABLE_HASHTAGS
        const val destTableName = "hashtags"

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
