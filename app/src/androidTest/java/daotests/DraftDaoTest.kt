package daotests

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase

import com.klinker.android.twitter_l.data.roomdb.entities.Draft

import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*

class DraftDaoTest : DaoTest() {

    private val draftDao get() = testDatabase.draftDao()

    @Test
    fun insertDraftTest() {
        val draft = Draft("This is the sample text for this draft", 1)
        val id = draftDao.insertDraft(draft)
        assertThat("Error trying to add draft to a database", id, not(-1L))
    }


    @Test
    fun deleteDraftTest() {
        val contentValues = ContentValues()
        val text = "This is the sample text for this draft"
        val account = 1

        contentValues.put("text", text)
        contentValues.put("account", account)

        val id = insertIntoDatabase("drafts", SQLiteDatabase.CONFLICT_IGNORE, contentValues)

        assertThat("Error trying to get the draft in the test database", id, not(-1L))
        val draft = Draft(id, text, account)

        draftDao.deleteDraft(draft)
        val cursor = queryDatabase("SELECT id FROM drafts", null)
        assertThat("Draft not deleted from the database", cursor.count, `is`(0))
        cursor.close()

    }


    @Test
    fun getDraftsTest() {

        val requestedAccount = 1
        val contentValues = ContentValues()

        contentValues.put("account", requestedAccount)

        beginTransaction()

        val firstAccountDraftsCount = (0..4).map {
            contentValues.put("text", "This is the sample text for account $it")
            insertIntoDatabase("drafts", SQLiteDatabase.CONFLICT_ABORT, contentValues)
        }.count { it != -1L }

        contentValues.put("account", requestedAccount + 1)

        val secondAccountDraftsCount = (0..4).map {
            contentValues.put("text", "This is the sample text for account $it")
            insertIntoDatabase("drafts", SQLiteDatabase.CONFLICT_ABORT, contentValues)
        }.count { it != -1L}

        endSuccessfulTransaction()
        assertThat("At least one draft from account $requestedAccount should be inserted to test this properly", firstAccountDraftsCount, greaterThan(0))
        assertThat("At least one draft from account ${requestedAccount + 1} should be inserted to test this properly", secondAccountDraftsCount, greaterThan(0))

        val drafts = draftDao.getDrafts(requestedAccount)

        assertThat("Incorrect number of drafts returned", drafts, hasSize(firstAccountDraftsCount))
        assertThat("All drafts must have the requested account number", drafts.all { it.account == requestedAccount })
        assertThat("All drafts must be in descending order by id",
                drafts.map { it.id },
                contains(drafts.asSequence().sortedByDescending {it.id}.map { `is`(it.id) }.toList()))

    }

    @Test
    fun deleteDrafts() {
        val contentValues = ContentValues()
        val requestedAccount = 1
        contentValues.put("account", requestedAccount)

        beginTransaction()

        val firstAccountIdsCount = (0..5).map {
            contentValues.put("text", "This is sample text for draft $it")
            insertIntoDatabase("drafts", SQLiteDatabase.CONFLICT_ABORT, contentValues)
        }.count { it != -1L }

        contentValues.put("account", requestedAccount + 1)

        val secondAccountIdsCount = (0..5).map {
            contentValues.put("text", "This is sample text for draft $it")
            insertIntoDatabase("drafts", SQLiteDatabase.CONFLICT_ABORT, contentValues)
        }.count { it != -1L }

        endSuccessfulTransaction()

        assertThat("There must be at least one inserted draft for account $requestedAccount to run this test properly", firstAccountIdsCount, greaterThan(0))
        assertThat("There must be at least one inserted draft for account ${requestedAccount + 1} to run this test properly", secondAccountIdsCount, greaterThan(0))

        draftDao.deleteDrafts(requestedAccount)

        val cursor1 = queryDatabase("SELECT id FROM drafts WHERE account = ?", arrayOf(requestedAccount))
        val cursor2 = queryDatabase("SELECT id FROM drafts WHERE account = ?", arrayOf(requestedAccount + 1))

        assertThat("Not all drafts from this account was deleted", cursor1.count, `is`(0))
        assertThat("Some drafts from a different account were deleted", cursor2.count, `is`(secondAccountIdsCount))

        cursor1.close()
        cursor2.close()
    }


    @After
    fun clearTables() {
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



