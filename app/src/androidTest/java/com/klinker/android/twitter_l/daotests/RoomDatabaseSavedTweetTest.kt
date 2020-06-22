package com.klinker.android.twitter_l.daotests

import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.klinker.android.twitter_l.data.roomdb.SavedTweetDao
import com.klinker.android.twitter_l.mockentities.MockSavedTweet
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.notNullValue
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
        val savedTweet = MockSavedTweet(1)
        val id = savedTweetDao.insert(savedTweet.savedTweet)
        assertThat("Did not return a valid id. Most likely a problem inserting entity into database", id, notNullValue())
        assertThat("Incorrect number of entries in database", database.size, equalTo(1))
    }

}