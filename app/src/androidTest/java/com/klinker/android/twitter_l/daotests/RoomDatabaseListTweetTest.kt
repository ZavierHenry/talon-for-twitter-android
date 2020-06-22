package com.klinker.android.twitter_l.daotests

import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.klinker.android.twitter_l.data.roomdb.ListTweetDao
import com.klinker.android.twitter_l.mockentities.MockListTweet
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
        val listTweet = MockListTweet(1)
        val id = listTweetDao.insert(listTweet.listTweet)
        assertThat("Did not return a valid id. Most likely a problem inserting entity into database", id, notNullValue())
        assertThat("Incorrect number of entries in database", database.size, equalTo(1))
    }

}