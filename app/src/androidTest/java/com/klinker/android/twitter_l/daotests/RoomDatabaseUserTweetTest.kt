package com.klinker.android.twitter_l.daotests

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.klinker.android.twitter_l.data.roomdb.UserTweetDao
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class RoomDatabaseUserTweetTest {

    private lateinit var userTweetDao: UserTweetDao

    @get:Rule val database = TestDatabase("user_tweets")

    @Before
    fun getUserTweetDao() {
        userTweetDao = database.database.userTweetDao()
    }

    @Test
    @Throws(Exception::class)
    fun testInsertUserTweet() {

    }

}