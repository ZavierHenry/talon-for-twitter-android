package com.klinker.android.twitter_l

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.klinker.android.twitter_l.data.roomdb.QueuedTweetDao
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class RoomDatabaseQueuedTweetTest {

    private lateinit var queuedTweetDao: QueuedTweetDao

    @get:Rule val database = TestDatabase("queued_tweet")


    @Before
    fun getQueuedTweetDao() {
        queuedTweetDao = database.database.queuedTweetDao()
    }


}