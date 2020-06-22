package com.klinker.android.twitter_l.daotests

import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.klinker.android.twitter_l.data.roomdb.UserTweetDao
import com.klinker.android.twitter_l.mockentities.MockUserTweet
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.notNullValue
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
        val userTweet = MockUserTweet(1)
        val id = userTweetDao.insert(userTweet.userTweet)
        assertThat("Did not return a valid id. Most likely a problem inserting entity into database", id, notNullValue())
        assertThat("Incorrect number of entries in database", database.size, equalTo(1))
    }

}