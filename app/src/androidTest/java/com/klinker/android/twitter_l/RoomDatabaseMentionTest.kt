package com.klinker.android.twitter_l

import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.klinker.android.twitter_l.data.roomdb.MentionDao
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class RoomDatabaseMentionTest {

    private lateinit var mentionDao: MentionDao

    @get:Rule val database = TestDatabase("mentions")

    @Before
    fun getMentionDao() {
        mentionDao = database.database.mentionDao()
    }

    @Test
    @Throws(Exception::class)
    fun testInsertMention() {
        val mention = MockMention(1)
        val id = mentionDao.insertMention(mention.mention)
        assertThat(id, notNullValue())
        assertThat(database.size, equalTo(1))
    }

}