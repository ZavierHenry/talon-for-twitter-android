package com.klinker.android.twitter_l.daotests

import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.klinker.android.twitter_l.data.roomdb.ListTweetDao
import com.klinker.android.twitter_l.mockentities.MockListTweet
import com.klinker.android.twitter_l.mockentities.matchers.EntityValidIdMatcher.Companion.hasValidId
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class RoomDatabaseListTweetTest {

    private lateinit var listTweetDao: ListTweetDao

    @get:Rule val database = TestDatabase("list_tweets")

    @Before
    fun getListTweetDao() {
        listTweetDao = database.database.listTweetDao()
    }

    @Test
    @Throws(Exception::class)
    fun testInsertListTweet() {
        val listTweet = listTweetDao.insert(MockListTweet(1).listTweet)
        assertThat("Invalid id", MockListTweet(listTweet), hasValidId())
        assertThat("Incorrect number of entries in database", database.size, equalTo(1))
    }

}