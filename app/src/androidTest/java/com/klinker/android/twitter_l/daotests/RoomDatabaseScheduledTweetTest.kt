package com.klinker.android.twitter_l.daotests

import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.klinker.android.twitter_l.data.roomdb.ScheduledTweetDao
import com.klinker.android.twitter_l.mockentities.MockSavedTweet
import com.klinker.android.twitter_l.mockentities.MockScheduledTweet
import com.klinker.android.twitter_l.mockentities.matchers.EntityValidIdMatcher.Companion.hasValidId
import org.hamcrest.CoreMatchers.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*


@RunWith(AndroidJUnit4ClassRunner::class)
class RoomDatabaseScheduledTweetTest {

    private lateinit var scheduledTweetDao: ScheduledTweetDao

    @get:Rule val database = TestDatabase("scheduled_tweets")

    @Before
    fun getScheduledTweetDao() {
        scheduledTweetDao = database.database.scheduledTweetDao()
    }
    
    @Test
    @Throws(Exception::class)
    fun testInsertScheduledTweet() {
        val scheduledTweet = scheduledTweetDao.insert(MockScheduledTweet(1).scheduledTweet)
        assertThat("Invalid id", MockScheduledTweet(scheduledTweet), hasValidId())
        assertThat("Incorrect number of entries in database", database.size, equalTo(1))
    }

    @Test
    @Throws(Exception::class)
    fun testGetNextScheduledTime_EmptyTable() {
        val time = scheduledTweetDao.getNextScheduledTime(Date().time)
        assertThat("Getting next scheduled time does not return null if it doesn't exist", time, nullValue())
    }

    @Test
    @Throws(Exception::class)
    fun testGetNextScheduledTime_NoTime() {
        val firstScheduledTweet = MockScheduledTweet(1, time = 1000L, alarmId = 1L)
        val secondScheduledTweet = MockScheduledTweet(1, time = 2000L, alarmId = 2L)

        database.insertIntoDatabase(firstScheduledTweet)
        database.insertIntoDatabase(secondScheduledTweet)

        assertThat("Initial setup entities did not insert into database properly", database.size, equalTo(2))
        val time = scheduledTweetDao.getNextScheduledTime(3000L)
        assertThat("Returned a value when the value should be null", time, nullValue())
    }

    @Test
    @Throws(Exception::class)
    fun testGetNextScheduledTime_Time() {

        val scheduledTweets = List(3) {
            MockScheduledTweet(1, time = 1000L * it, alarmId = (it+1).toLong())
        }

        scheduledTweets.forEach { database.insertIntoDatabase(it) }

        assertThat("Initial setup entities did not insert into database properly", database.size, equalTo(scheduledTweets.size))
        val time = scheduledTweetDao.getNextScheduledTime(1500L)
        assertThat("Incorrect time was retrieved from next scheduled time", time, equalTo(2000L))
    }

    @Test
    @Throws(Exception::class)
    fun testGetNextScheduledTweet_EmptyTable() {
        val scheduledTweet = scheduledTweetDao.getNextScheduledTweet(Date().time - (60 * 60 * 1000))
        assertThat("Getting next scheduled message does not return null if it doesn't exist", scheduledTweet, nullValue())
    }

    @Test
    @Throws(Exception::class)
    fun testGetNextScheduledTweet_NoTweet() {
        val scheduledTweets = List(2) {
            MockScheduledTweet(1, time = 1000L * it, alarmId = (it+1).toLong())
        }

        scheduledTweets.forEach { database.insertIntoDatabase(it) }

        assertThat("Initial setup entities did not insert into database properly", database.size, equalTo(scheduledTweets.size))
        val scheduledTweet = scheduledTweetDao.getNextScheduledTweet(3000L)
        assertThat("Getting next scheduled message does not return null when it should with non-empty database", scheduledTweet, nullValue())
    }


}