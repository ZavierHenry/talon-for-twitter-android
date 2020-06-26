package com.klinker.android.twitter_l.daotests

import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.klinker.android.twitter_l.data.roomdb.SavedTweetDao
import com.klinker.android.twitter_l.mockentities.MockSavedTweet
import com.klinker.android.twitter_l.mockentities.matchers.EntityValidIdMatcher.Companion.hasValidId
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
        val savedTweet = savedTweetDao.insert(MockSavedTweet(1).savedTweet)
        assertThat("Invalid id", MockSavedTweet(savedTweet), hasValidId())
        assertThat("Incorrect number of entries in database", database.size, equalTo(1))
    }

}