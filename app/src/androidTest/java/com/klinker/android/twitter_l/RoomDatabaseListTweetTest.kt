package com.klinker.android.twitter_l

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.klinker.android.twitter_l.data.roomdb.ListTweet
import com.klinker.android.twitter_l.data.roomdb.ListTweetDao
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class RoomDatabaseListTweetTest {

    private lateinit var listTweetDao: ListTweetDao

    @get:Rule val database = TestDatabase("list_tweets")


    @Before
    fun getListTweetDao() {
        listTweetDao = database.database.listTweetDao()
    }

}