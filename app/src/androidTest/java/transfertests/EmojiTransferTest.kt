package transfertests

import android.content.ContentValues
import android.database.Cursor

import com.klinker.android.twitter_l.data.roomdb.TalonDatabase
import com.klinker.android.twitter_l.data.sq_lite.EmojiSQLiteHelper


import org.junit.After
import org.junit.AfterClass

import org.junit.BeforeClass
import org.junit.Test

import java.util.ArrayList

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*

import transfertests.MockEmojiMatcher.Companion.matchesEmoji


class EmojiTransferTest : TransferTest() {


    @Test
    fun testBasicTransferResult() {

        val emojis = List(2) { MockEmoji("This is the text for emoji $it", "(^($it)^)", it + 1) }
        val contentValues = ContentValues()


        val idCount = asTransaction {
            emojis.asSequence().map { emoji ->
                emoji.setContentValues(contentValues)
                insertIntoSourceDatabase(EmojiSQLiteHelper.TABLE_RECENTS, contentValues)
            }.count { it != -1L }
        }

        assertThat("At least one successful insertion is necessary to run this test", idCount, greaterThan(0))

        applyCallback(TalonDatabase.transferEmojiData(context, sourceDatabasePath))

        val cursor = queryTestDatabase("SELECT * FROM emojis", null)
        assertThat("Number of transfers is incorrect", cursor.count, `is`(idCount))
        assertThat("Problem moving to first value of the database", cursor.moveToFirst())


        val matchers = emojis.map { matchesEmoji(it) }

        do {

            val testEmoji = MockEmoji.create(cursor)
            assertThat("Database transfer did not work properly", testEmoji, anyOf(matchers))

        } while (cursor.moveToNext())

        cursor.close()

    }


    @Test
    fun testDuplicateIcons() {
        val contentValues = ContentValues()
        val emojis = List(3) { MockEmoji("This is text for the emoji $it", "22",  it)}

        asTransaction {

            emojis.forEach { emoji ->
                emoji.setContentValues(contentValues)
                insertIntoSourceDatabase(EmojiSQLiteHelper.TABLE_RECENTS, contentValues)
            }

        }

        applyCallback(TalonDatabase.transferEmojiData(context, sourceDatabasePath))

        queryTestDatabase("SELECT * FROM emojis", null).use { cursor ->
            assertThat("Incorrect number of emojis in the database", cursor.count, `is`(1))
            assertThat("Error pointing to value in the database", cursor.moveToFirst())

            val testEmoji = MockEmoji.create(cursor)
            assertThat("Proper emoji did not transfer into the new database", testEmoji, matchesEmoji(emojis.maxBy { it.count!! }!!))
        }

    }


    @Test
    fun testDuplicateText() {
        val contentValues = ContentValues()
        val emojis = List(3) { MockEmoji( "This is the duplicate text for the emoji", "$it", it)}

        asTransaction {

            emojis.forEach { emoji ->
                emoji.setContentValues(contentValues)
                insertIntoSourceDatabase(EmojiSQLiteHelper.TABLE_RECENTS, contentValues)
            }
        }

        applyCallback(TalonDatabase.transferEmojiData(context, sourceDatabasePath))

        queryTestDatabase("SELECT * FROM emojis", null).use { cursor ->
            assertThat("Incorrect number of emojis in the database", cursor.count, `is`(1))
            assertThat("Error pointing to value in the database", cursor.moveToFirst())
            val testEmoji = MockEmoji.create(cursor)
            assertThat("Proper emoji did not transfer into the new database", testEmoji, matchesEmoji(emojis.maxBy { it.count!! }!!))
        }

    }


    @Test
    fun testTransferIfEmptyTable() {

        applyCallback(TalonDatabase.transferEmojiData(context, sourceDatabasePath))
        queryTestDatabase("SELECT * FROM emojis", null).use { cursor ->
            assertThat("Database table somehow got populated", cursor.count, `is`(0))
        }

    }


    @Test
    fun testCallbackIfNoSourceDatabase() {
        applyCallback(TalonDatabase.transferEmojiData(context, TransferTest.badDatabaseLocation))
    }

    @After
    fun clearDatabases() {
        clearTestDatabase()
        clearSourceDatabase(EmojiSQLiteHelper.TABLE_RECENTS)
    }

    companion object {

        @BeforeClass
        @JvmStatic fun initDatabase() {
            val tableCreation = EmojiSQLiteHelper.DATABASE_CREATE
            TransferTest.initSourceDatabase(tableCreation)
            TransferTest.initTestDatabase()
        }


        @AfterClass
        @JvmStatic fun closeDatabase() {
            TransferTest.closeTestDatabase()
            TransferTest.closeSourceDatabase()
        }
    }

}


internal class MockEmoji(var text: String?, var icon: String?, var count: Int?) : MockEntity<MockEmoji>() {
    override fun showMismatches(other: MockEmoji): Collection<FieldMismatch> {
        val mismatches = ArrayList<FieldMismatch>()

        if (text != other.text) {
            mismatches.add(makeMismatch("text", text, other.text))
        }

        if (icon != other.icon) {
            mismatches.add(makeMismatch("icon", icon, other.icon))
        }

        if (count != other.count) {
            mismatches.add(makeMismatch("count", count, other.count))
        }

        return mismatches
    }

    override fun setContentValues(contentValues: ContentValues) {

        with(contentValues) {
            put(EmojiSQLiteHelper.COLUMN_TEXT, text)
            put(EmojiSQLiteHelper.COLUMN_ICON, icon)
            put(EmojiSQLiteHelper.COLUMN_COUNT, count)
        }
    }

    companion object {

        @JvmStatic fun create(cursor: Cursor) : MockEmoji {
            val text = cursor.getString(cursor.getColumnIndex("text"))
            val icon = cursor.getString(cursor.getColumnIndex("icon"))
            val count = cursor.getInt(cursor.getColumnIndex("count"))

            return MockEmoji(text, icon, count)
        }

    }


}


internal class MockEmojiMatcher private constructor(expected: MockEmoji) : MockMatcher<MockEmoji>(expected) {

    companion object {
        @JvmStatic fun matchesEmoji(expected: MockEmoji) : MockEmojiMatcher {
            return MockEmojiMatcher(expected)
        }
    }
}
