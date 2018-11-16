package daotests

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.strictmode.SqliteObjectLeakedViolation

import com.klinker.android.twitter_l.data.roomdb.entities.Emoji
import com.klinker.android.twitter_l.data.sq_lite.EmojiSQLiteHelper

import org.junit.After
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.greaterThan
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.not
import org.hamcrest.Matchers.notNullValue

class EmojiDaoTest : DaoTest() {

    private val emojiDao get() = testDatabase.emojiDao()

    @Test
    fun insertEmojiTest() {
        val emoji = Emoji("Smiley face", ":}")
        emoji.count = 33
        emojiDao.insertEmoji(emoji)

        val cursor = queryDatabase("SELECT * FROM emojis", null)

        assertThat("Emoji did not insert properly into database", cursor.count, `is`(1))
        assertThat("Emoji did not insert properly into database", cursor.moveToFirst(), `is`(true))
        assertThat("Emoji text not saved properly", cursor.getString(cursor.getColumnIndex("text")), `is`(emoji.text))
        assertThat("Emoji icon was not saved properly", cursor.getString(cursor.getColumnIndex("icon")), `is`(emoji.icon))
        assertThat("Emoji count was not saved properly", cursor.getInt(cursor.getColumnIndex("count")), `is`(emoji.count))
        cursor.close()
    }

    @Test
    fun insertEmojiWithDuplicateIcons() {
        val emoji = Emoji("Smiley face", ":>")
        emoji.count = 44

        val contentValues = ContentValues()
        contentValues.put("text", emoji.text)
        contentValues.put("icon", emoji.icon)
        contentValues.put("count", emoji.count)

        val id = insertIntoDatabase("emojis", SQLiteDatabase.CONFLICT_ABORT, contentValues)
        assertThat("Database setup did not execute correctly", id, not<Number>(-1))

        val oldValue = emoji.count
        emoji.count = emoji.count + 243

        emojiDao.insertEmoji(emoji)
        val cursor = queryDatabase("SELECT * FROM emojis", null)
        assertThat("Database saved when it shouldn't have saved", cursor.count, `is`(1))

        cursor.moveToFirst() //add as assertion
        assertThat("New value was saved instead of the old value", cursor.getInt(cursor.getColumnIndex("count")), `is`(oldValue))

    }


    @Test
    fun deleteEmojiTest() {
        val text = "Smiley face"
        val icon = ":)"
        val count = 44

        val contentValues = ContentValues()
        contentValues.put("icon", icon)
        contentValues.put("text", text)
        contentValues.put("count", count)

        val id = insertIntoDatabase("emojis", SQLiteDatabase.CONFLICT_ABORT, contentValues)
        assertThat("Emoji must be in database to properly test this", id, not(-1L))

        val emoji = Emoji(id, text, icon, count)
        emojiDao.deleteEmoji(emoji)

        val cursor = queryDatabase("SELECT * FROM emojis", null)
        assertThat("Element did not delete properly", cursor.count, `is`(0))
        cursor.close()

    }

    //increment emoji count test

    @Test
    fun getRecents() {

    }

    @Test
    fun deleteLeastUsedRecents() {

    }


    @Test
    fun deleteLeastUsedRecentsOutsideBounds() {

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
