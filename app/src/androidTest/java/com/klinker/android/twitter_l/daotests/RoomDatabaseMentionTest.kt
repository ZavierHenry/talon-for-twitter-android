package com.klinker.android.twitter_l.daotests

import androidx.test.espresso.internal.inject.InstrumentationContext
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.klinker.android.twitter_l.mockentities.MockMention
import com.klinker.android.twitter_l.data.roomdb.MentionDao
import com.klinker.android.twitter_l.mockentities.matchers.EntityValidIdMatcher.Companion.hasValidId
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
        val mention = mentionDao.insert(MockMention(1).mention)
        assertThat("Invalid id", MockMention(mention), hasValidId())
        assertThat("Incorrect number of entries in database", database.size, equalTo(1))
    }

}