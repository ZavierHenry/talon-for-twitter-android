package com.klinker.android.twitter_l

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.klinker.android.twitter_l.data.roomdb.ScheduledTweetDao
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class RoomDatabaseScheduledTweetTest {

    private lateinit var scheduledTweetDao: ScheduledTweetDao

    @get:Rule val database = TestDatabase("scheduled_tweet")

    @Before
    fun getScheduledTweetDao() {
        scheduledTweetDao = database.database.scheduledTweetDao()
    }
    
    @Test
    @Throws(Exception::class)
    fun testInsertScheduledTweet() {

    }

}