package transfertests

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

import com.klinker.android.twitter_l.data.roomdb.TalonDatabase
import com.klinker.android.twitter_l.data.roomdb.entities.Emoji
import com.klinker.android.twitter_l.data.sq_lite.EmojiSQLiteHelper

import org.hamcrest.Description
import org.hamcrest.Matchers
import org.hamcrest.TypeSafeDiagnosingMatcher
import org.hamcrest.TypeSafeMatcher
import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

import java.io.File
import java.util.ArrayList
import java.util.HashMap
import java.util.HashSet

import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.test.platform.app.InstrumentationRegistry

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.containsInAnyOrder
import org.hamcrest.Matchers.empty
import org.hamcrest.Matchers.greaterThan
import org.hamcrest.Matchers.hasSize
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.not
import org.hamcrest.Matchers.notNullValue

import transfertests.MockEmojiMatcher.Companion.matchesEmoji

class EmojiTransferTest : TransferTest() {


    @Test
    fun testBasicTransferResult() {
        val insertedEmojis = HashMap<String, MockEmoji>()

        val contentValues = ContentValues()
        TransferTest.sourceDatabase!!.beginTransaction()

        for (i in 0..29) {

            val emoji = MockEmoji("icon_$i", "Description for icon $i", i)
            emoji.setContentValues(contentValues)

            val id = TransferTest.sourceDatabase!!.insert(EmojiSQLiteHelper.TABLE_RECENTS, null, contentValues)
            if (id != -1L) {
                insertedEmojis[emoji.icon] = emoji
            }
        }

        TransferTest.sourceDatabase!!.setTransactionSuccessful()
        TransferTest.sourceDatabase!!.endTransaction()

        assertThat("At least one successful insertion is necessary to run this test", insertedEmojis.size, greaterThan(0))

        applyCallback(TalonDatabase.transferEmojiData(TransferTest.sourceDatabase!!.path))

        val cursor = TransferTest.testDatabase!!.query("SELECT * FROM emojis", null)
        assertThat("Error getting database result for testing", cursor, notNullValue())
        assertThat("Number of transfers is incorrect", cursor.count, `is`(insertedEmojis.size))

        val actualEmojis = ArrayList<MockEmoji>()
        do {

            val testEmoji = MockEmoji(cursor)
            assertThat(testEmoji, matchesEmoji(insertedEmojis.remove(testEmoji.icon)!!))

        } while (cursor.moveToNext())

        cursor.close()

    }

    @Test
    fun testDuplicateIcons() {
        val expectedEmojis = HashMap<String, MockEmoji>()
        val contentValues = ContentValues()

        TransferTest.sourceDatabase!!.beginTransaction()

        for (i in 0..49) {
            val mockEmoji = MockEmoji("icon_" + i % 7, "Description for icon $i", i)

        }

        TransferTest.sourceDatabase!!.setTransactionSuccessful()
        TransferTest.sourceDatabase!!.endTransaction()

        //assert that the conditions for the test have been met

    }


    @Test
    fun testTransferIfEmptyTable() {

        applyCallback(TalonDatabase.transferEmojiData(TransferTest.sourceDatabase!!.path))
        val cursor = TransferTest.testDatabase!!.query("SELECT * FROM emojis", null)
        assertThat("Could not get database cursor", cursor, notNullValue())
        assertThat("Database table somehow got populated", cursor.count, `is`(0))
        cursor.close()
    }


    @Test
    fun testCallbackIfNoSourceDatabase() {
        applyCallback(TalonDatabase.transferEmojiData(TransferTest.badDatabaseLocation))
    }

    @After
    fun clearDatabases() {
        TransferTest.clearTestDatabase()
        TransferTest.clearSourceDatabase(EmojiSQLiteHelper.TABLE_RECENTS)
    }

    companion object {

        @BeforeClass
        fun initDatabase() {
            val tableCreation = EmojiSQLiteHelper.DATABASE_CREATE
            TransferTest.initSourceDatabase(tableCreation)
            TransferTest.initTestDatabase()
        }


        @AfterClass
        fun closeDatabase() {
            TransferTest.closeTestDatabase()
            TransferTest.closeSourceDatabase()
        }
    }


    //mock emoji matcher


}

internal class MockEmoji {

    var icon: String
    var text: String
    var count: Int

    constructor(icon: String, text: String, count: Int) {
        this.icon = icon
        this.text = text
        this.count = count

    }

    constructor(cursor: Cursor) {
        this.icon = cursor.getString(cursor.getColumnIndex("icon"))
        this.text = cursor.getString(cursor.getColumnIndex("text"))
        this.count = cursor.getInt(cursor.getColumnIndex("count"))
    }

    fun setContentValues(contentValues: ContentValues) {
        contentValues.put(EmojiSQLiteHelper.COLUMN_ICON, icon)
        contentValues.put(EmojiSQLiteHelper.COLUMN_TEXT, text)
        contentValues.put(EmojiSQLiteHelper.COLUMN_COUNT, count)
    }


}

internal class MockEmojiMatcher private constructor(private val expected: MockEmoji) : TypeSafeMatcher<MockEmoji>() {

    override fun matchesSafely(item: MockEmoji): Boolean {
        return expected.icon == item.icon && expected.text == item.text && expected.count == item.count
    }

    override fun describeTo(description: Description) {
        description.appendText("All emoji fields to be equal")
    }

    override fun describeMismatchSafely(item: MockEmoji, description: Description) {
        description.appendText("The following fields are not equal:\n")

        if (expected.icon != item.icon) {
            description.appendText("Expected icon ").appendValue(expected.icon)
                    .appendValue(", Actual icon ").appendValue(item.icon)
                    .appendValue("\n")
        }

        if (expected.text != item.text) {
            description.appendText("Expected text ").appendValue(expected.text)
                    .appendValue(", Actual text ").appendValue(item.text)
                    .appendValue("\n")
        }

        if (expected.count != item.count) {
            description.appendText("Expected count ").appendValue(expected.count)
                    .appendValue(", Actual count ").appendValue(item.count)
                    .appendValue("\n")
        }

    }

    companion object {

        fun matchesEmoji(expected: MockEmoji): MockEmojiMatcher {
            return MockEmojiMatcher(expected)
        }
    }

    //Possibly meta test matcher

}
