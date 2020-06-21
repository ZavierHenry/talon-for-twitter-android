package com.klinker.android.twitter_l.daotests

import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.klinker.android.twitter_l.data.roomdb.QueuedTweetDao
import com.klinker.android.twitter_l.mockentities.MockQueuedTweet
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class RoomDatabaseQueuedTweetTest {

    private lateinit var queuedTweetDao: QueuedTweetDao

    @get:Rule val database = TestDatabase("queued_tweets")


    @Before
    fun getQueuedTweetDao() {
        queuedTweetDao = database.database.queuedTweetDao()
    }

    @Test
    @Throws(Exception::class)
    fun testInsertQueuedTweet() {
        val queuedTweet = MockQueuedTweet(1)
        val id = queuedTweetDao.insert(queuedTweet.queuedTweet)
        assertThat(id, notNullValue())
        assertThat(database.size, equalTo(1))
    }


}