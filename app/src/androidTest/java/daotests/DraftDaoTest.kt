package daotests

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

import com.klinker.android.twitter_l.data.roomdb.entities.Draft

import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.not

class DraftDaoTest : DaoTest() {


    @Test
    fun insertDraftTest() {
        val draft = Draft("This is the sample text for this draft", 1)
        val id = DaoTest.testDatabase!!.draftDao().insertDraft(draft)
        assertThat("Error trying to add draft to a database", id, not<Number>(-1))
        val cursor = DaoTest.testDatabase!!.query("SELECT id FROM drafts", null)
        assertThat("Draft was not properly saved into the database", cursor.count, `is`(1))
        cursor.close()
    }


    @Test
    fun deleteDraftTest() {
        val contentValues = ContentValues()
        val text = "This is the sample text for this draft"
        val account = 1

        contentValues.put("text", text)
        contentValues.put("account", account)
        val id = DaoTest.testDatabase!!.openHelper.writableDatabase.insert("drafts", SQLiteDatabase.CONFLICT_IGNORE, contentValues)

        assertThat("Error trying to get the draft in the test database", id, not<Number>(-1))
        val draft = Draft(id, text, account)

        var cursor = DaoTest.testDatabase!!.query("SELECT id FROM drafts", null)
        assertThat("Value id not initially save in database", cursor.count, `is`(1))
        cursor.close()

        DaoTest.testDatabase!!.draftDao().deleteDraft(draft)
        cursor = DaoTest.testDatabase!!.query("SELECT id FROM drafts", null)
        assertThat("Draft not deleted from the database", cursor.count, `is`(0))
        cursor.close()

    }

    @Test
    fun deleteDraftById() {
        val contentValues = ContentValues()
        val text = "This is the sample text for this draft"
        val account = 1

        contentValues.put("text", text)
        contentValues.put("account", account)
        val id = DaoTest.testDatabase!!.openHelper.writableDatabase.insert("drafts", SQLiteDatabase.CONFLICT_IGNORE, contentValues)
        assertThat("Test must be inserted into database to run test", id, not<Number>(-1))
        var cursor = DaoTest.testDatabase!!.query("SELECT id FROM drafts", null)
        assertThat("Draft did not dave to the database", cursor.count, `is`(1))
        cursor.close()

        DaoTest.testDatabase!!.draftDao().deleteDraft(id)
        cursor = DaoTest.testDatabase!!.query("SELECT id FROM drafts", null)
        assertThat("Draft did not delete from the database", cursor.count, `is`(0))
        cursor.close()
    }


    @Test
    fun getDraftsTest() {
        val contentValues = ContentValues()
        val testSize = 5

        DaoTest.testDatabase!!.openHelper.writableDatabase.beginTransaction()

        for (i in 0 until testSize) {
            val text = "This is the sample text for draft $i"
            val account = i % 2
        }

        DaoTest.testDatabase!!.openHelper.writableDatabase.setTransactionSuccessful()
        DaoTest.testDatabase!!.openHelper.writableDatabase.endTransaction()

    }

    @After
    fun clearTables() {
        DaoTest.clearTestDatabase()
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



