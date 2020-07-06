package com.klinker.android.twitter_l.daotests

import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.klinker.android.twitter_l.data.roomdb.HomeTweetDao
import com.klinker.android.twitter_l.mockentities.MockHomeTweet
import com.klinker.android.twitter_l.mockentities.MockUtilities
import com.klinker.android.twitter_l.mockentities.matchers.EntityValidIdMatcher.Companion.hasValidId
import com.klinker.android.twitter_l.mockentities.matchers.MockEntityMatcher.Companion.matchesMockEntity
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.Matcher
import org.hamcrest.Matchers.contains
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

    @Test
    @Throws(Exception::class)
    fun testTrimDatabase_NoTrim() {
        val homeTweets = List(10) {
            MockHomeTweet(1, tweet = MockUtilities.makeMockTweet(tweetId = it.toLong() + 1))
        }

        homeTweets.forEach { database.insertIntoDatabase(it) }

        assertThat("Problem setting up initial entities in database", database.size, equalTo(homeTweets.size))
        homeTweetDao.trimDatabase(1, 20)
        assertThat("Number of entries in database changed", database.size, equalTo(homeTweets.size))

    }

    @Test
    @Throws(Exception::class)
    fun testTrimDatabase_NoTrim_RespectAccount() {
        val homeTweets = List(10) {
            MockHomeTweet(1, tweet = MockUtilities.makeMockTweet(tweetId = it.toLong() + 1))
        }

        val secondAccountHomeTweet = MockHomeTweet(2, tweet = MockUtilities.makeMockTweet(tweetId = 500L))
        homeTweets.forEach { database.insertIntoDatabase(it) }
        database.insertIntoDatabase(secondAccountHomeTweet)

        assertThat("Problem setting up initial entries in database", database.size, equalTo(homeTweets.size + 1))
        homeTweetDao.trimDatabase(1, homeTweets.size)
        assertThat("Trimming home tweets does not respect the account of the home tweet", database.size, equalTo(homeTweets.size + 1))

    }

    @Test
    @Throws(Exception::class)
    fun testTrimDatabase_BasicTrim() {
        val homeTweets = List(20) {
            MockHomeTweet(1, tweet = MockUtilities.makeMockTweet(
                    tweetId = it.toLong() + 3,
                    time = it.toLong() + 15,
                    author = MockUtilities.makeMockUser(name = "Chris Hayes $it")))
        }

        val insertedHomeTweets = homeTweets.map { homeTweet ->
            val id = database.insertIntoDatabase(homeTweet)
            homeTweet.copy(homeTweet = homeTweet.homeTweet.copy(id = id))
        }

        assertThat("Problem setting up intital entites in database", database.size, equalTo(homeTweets.size))
        homeTweetDao.trimDatabase(1, homeTweets.size - 5)

        assertThat("Incorrect number of entities trimmed from the home tweets table", database.size, equalTo(homeTweets.size - 5))
        val expectedUntrimmedHomeTweets = insertedHomeTweets
                .sortedByDescending { it.id }
                .take(homeTweets.size - 5)

        val untrimmedHomeTweets = database.queryFromDatabase("SELECT * FROM home_tweets ORDER BY id DESC").use { cursor ->
            cursor.moveToFirst()
            val mutableEntities = MutableList(0) { matchesMockEntity(MockHomeTweet(1)) }
            while (!cursor.isAfterLast) {
                val homeTweet = MockHomeTweet(cursor)
                mutableEntities.add(matchesMockEntity(homeTweet))
                cursor.moveToNext()
            }
            mutableEntities.toList()
        }

        assertThat("Wrong entities were kept in the table after trimming", expectedUntrimmedHomeTweets, contains(untrimmedHomeTweets))
    }

}