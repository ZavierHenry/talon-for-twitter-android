package com.klinker.android.twitter_l

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.klinker.android.twitter_l.data.roomdb.MentionDao
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

    }

}