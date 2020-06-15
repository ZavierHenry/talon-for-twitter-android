package com.klinker.android.twitter_l

import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.klinker.android.twitter_l.data.roomdb.Draft
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
        val draft = Draft("", 1)
        val id = draftDao.insertDraft(draft)
        assertThat(id, notNullValue())
        assertThat(database.size, equalTo(1))
    }

    @Test
    @Throws(Exception::class)
    fun testDeleteDraft() {
        val draft = Draft("", 1)
        val mockDraft = MockDraft(draft)
        val id = database.insertIntoDatabase(mockDraft)

        assertThat(id, notNullValue())
        assertThat(database.size, equalTo(1))

        draftDao.deleteDraft(draft.copy(id = id))
        assertThat(database.size, equalTo(0))
    }

    @Test
    @Throws(Exception::class)
    fun testUpdateDraft() {
        val draft = Draft("old draft", 1)
        val mockDraft = MockDraft(draft)
        val id = database.insertIntoDatabase(mockDraft)

        assertThat(id, notNullValue())
        assertThat(database.size, equalTo(1))

        draftDao.updateDraft(draft.copy(id = id, text = "new draft"))
        //get new draft from the database
        //assert that text equals the new value
    }

}