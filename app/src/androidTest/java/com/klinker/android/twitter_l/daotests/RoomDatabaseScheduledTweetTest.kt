package com.klinker.android.twitter_l.daotests

import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.klinker.android.twitter_l.data.roomdb.ScheduledTweetDao
import com.klinker.android.twitter_l.mockentities.MockSavedTweet
import com.klinker.android.twitter_l.mockentities.MockScheduledTweet
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class RoomDatabaseScheduledTweetTest {

    private lateinit var scheduledTweetDao: ScheduledTweetDao

    @get:Rule val database = TestDatabase("scheduled_tweets")

    @Before
    fun getScheduledTweetDao() {
        scheduledTweetDao = database.database.scheduledTweetDao()
    }
    
    @Test
    @Throws(Exception::class)
    fun testInsertScheduledTweet() {
        val scheduledTweet = MockScheduledTweet(1)
        val id = scheduledTweetDao.insert(scheduledTweet.scheduledTweet)
        assertThat(id, notNullValue())
        assertThat(database.size, equalTo(1))
    }

}