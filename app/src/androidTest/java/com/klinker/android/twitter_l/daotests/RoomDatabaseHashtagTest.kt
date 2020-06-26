package com.klinker.android.twitter_l.daotests

import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.klinker.android.twitter_l.data.roomdb.HashtagDao
import com.klinker.android.twitter_l.mockentities.MockHashtag
import com.klinker.android.twitter_l.mockentities.matchers.EntityValidIdMatcher.Companion.hasInvalidId
import com.klinker.android.twitter_l.mockentities.matchers.EntityValidIdMatcher.Companion.hasValidId
import org.hamcrest.CoreMatchers.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class RoomDatabaseHashtagTest {

    private lateinit var hashtagDao: HashtagDao

    @get:Rule val database = TestDatabase("hashtags")

    @Before
    fun getHashtagDao() {
        hashtagDao = database.database.hashtagDao()
    }

    @Test
    @Throws(Exception::class)
    fun testInsertHashtag() {
        val hashtag = hashtagDao.insert(MockHashtag().hashtag)
        assertThat(MockHashtag(hashtag), hasValidId())
        assertThat(database.size, equalTo(1))
    }


    @Test
    @Throws(Exception::class)
    fun testDeleteHashtag() {
        val hashtag = MockHashtag("#nascar")
        val id = database.insertIntoDatabase(hashtag)
        assertThat(id, not(equalTo(-1L)))
        assertThat(database.size, equalTo(1))

        hashtagDao.delete(hashtag.hashtag.copy(id = id))
        assertThat(database.size, equalTo(0))
    }


    @Test
    @Throws(Exception::class)
    fun testTagUniqueness() {
        val hashtag = MockHashtag("#nascar")
        val id = database.insertIntoDatabase(hashtag)
        assertThat(id, not(equalTo(-1L)))
        assertThat(database.size, equalTo(1))

        val duplicate = hashtagDao.insert(hashtag.copy().hashtag)
        assertThat(MockHashtag(duplicate), hasInvalidId())
        assertThat(database.size, equalTo(1))

    }

}