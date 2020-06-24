package com.klinker.android.twitter_l.daotests

import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.klinker.android.twitter_l.data.roomdb.HomeTweetDao
import com.klinker.android.twitter_l.mockentities.MockHomeTweet
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class RoomDatabaseHomeTweetTest {

    private lateinit var homeTweetDao: HomeTweetDao

    @get:Rule val database = TestDatabase("home_tweets")

    @Before
    fun getHomeTweetDao() {
        homeTweetDao = database.database.homeTweetDao()
    }

    @Test
    @Throws(Exception::class)
    fun testInsertHomeTweet() {
        val homeTweet = MockHomeTweet(1)
        val id = homeTweetDao.insert(homeTweet.homeTweet)
        assertThat("Did not return a valid id. Most likely a problem inserting entity into database", id, notNullValue())
        assertThat("Incorrect number of entries in database", database.size, equalTo(1))
    }

}