package com.klinker.android.twitter_l.daotests

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.klinker.android.twitter_l.data.roomdb.SavedTweetDao
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class RoomDatabaseSavedTweetTest {

    private lateinit var savedTweetDao: SavedTweetDao

    @get:Rule val database = TestDatabase("saved_tweets")


    @Before
    fun getSavedTweetDao() {
        savedTweetDao = database.database.savedTweetDao()
    }

    @Test
    @Throws(Exception::class)
    fun testInsertSavedTweet() {

    }

}