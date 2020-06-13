package com.klinker.android.twitter_l

import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.klinker.android.twitter_l.data.roomdb.Draft
import com.klinker.android.twitter_l.data.roomdb.DraftDao
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class RoomDatabaseDraftTest {
    private lateinit var draftDao: DraftDao

    private fun makeMockDraft(account: Int, text: String = "") : Draft {
        return Draft(text, account)
    }

    @get:Rule val database = TestDatabase()

    @Before
    fun getDraftDao() {
        draftDao = database.database.draftDao()
    }

    @Test
    @Throws(Exception::class)
    fun testInsertDraft() {
        val draft = makeMockDraft(1)
        val id = draftDao.insertDraft(draft)
        assertThat(id, notNullValue())
        val drafts = draftDao.getDrafts(1)
        assertThat(drafts.size, equalTo(1))
        assertThat(drafts[0].id, equalTo(id))
    }

    @Test
    @Throws(Exception::class)
    fun testDeleteDraft() {
        val draft = makeMockDraft(1)
        val id = draftDao.insertDraft(draft)
        assertThat(id, notNullValue())
        draftDao.deleteDraft(draft.copy(id = id))
        val draftsSize = draftDao.getDrafts(1).size
        assertThat(draftsSize, equalTo(0))
    }



}