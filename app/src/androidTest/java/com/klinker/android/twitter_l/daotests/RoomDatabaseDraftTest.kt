package com.klinker.android.twitter_l.daotests

import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.klinker.android.twitter_l.mockentities.MockDraft
import com.klinker.android.twitter_l.data.roomdb.DraftDao
import org.hamcrest.CoreMatchers.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class RoomDatabaseDraftTest {
    private lateinit var draftDao: DraftDao

    @get:Rule val database = TestDatabase("drafts")

    @Before
    fun getDraftDao() {
        draftDao = database.database.draftDao()
    }

    @Test
    @Throws(Exception::class)
    fun testInsertDraft() {
        val draft = MockDraft(1)
        val id = draftDao.insertDraft(draft.draft)
        assertThat(id, notNullValue())
        assertThat(database.size, equalTo(1))
    }

    @Test
    @Throws(Exception::class)
    fun testDeleteDraft() {
        val draft = MockDraft(1)
        val id = database.insertIntoDatabase(draft)

        assertThat(id, notNullValue())
        assertThat(database.size, equalTo(1))

        draftDao.deleteDraft(draft.draft.copy(id = id))
        assertThat(database.size, equalTo(0))
    }

    @Test
    @Throws(Exception::class)
    fun testUpdateDraft() {
        val draft = MockDraft(1, "old draft")
        val id = database.insertIntoDatabase(draft)

        assertThat(id, notNullValue())
        assertThat(database.size, equalTo(1))

        draftDao.updateDraft(draft.draft.copy(id = id, text = "new draft"))
        database.queryFromDatabase("SELECT * FROM drafts WHERE id = ?", arrayOf(id as Any)).use { cursor ->
            cursor.moveToFirst()
            val databaseDraft = MockDraft(cursor)
            assertThat(databaseDraft.draft.text, equalTo("new draft"))
        }

    }

}