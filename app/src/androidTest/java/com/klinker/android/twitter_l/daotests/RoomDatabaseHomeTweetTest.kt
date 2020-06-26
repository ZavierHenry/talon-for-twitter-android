package com.klinker.android.twitter_l.daotests

import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.klinker.android.twitter_l.data.roomdb.HomeTweetDao
import com.klinker.android.twitter_l.mockentities.MockHomeTweet
import com.klinker.android.twitter_l.mockentities.matchers.EntityValidIdMatcher.Companion.hasValidId
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
        val homeTweet = homeTweetDao.insert(MockHomeTweet(1).homeTweet)
        assertThat("Invalid id", MockHomeTweet(homeTweet), hasValidId())
        assertThat("Incorrect number of entries in database", database.size, equalTo(1))
    }

}