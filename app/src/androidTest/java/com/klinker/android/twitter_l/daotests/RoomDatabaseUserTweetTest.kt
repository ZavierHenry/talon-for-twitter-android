package com.klinker.android.twitter_l.daotests

import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.klinker.android.twitter_l.data.roomdb.UserTweetDao
import com.klinker.android.twitter_l.mockentities.MockUserTweet
import com.klinker.android.twitter_l.mockentities.matchers.EntityValidIdMatcher.Companion.hasValidId
import org.hamcrest.CoreMatchers.*
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
        val userTweet = userTweetDao.insert(MockUserTweet(1).userTweet)
        assertThat("Incorrect number of entries in database", database.size, equalTo(1))
        assertThat(userTweet.id, not(equalTo(-1L)))
        assertThat("Invalid id", MockUserTweet(userTweet), hasValidId())
    }

}