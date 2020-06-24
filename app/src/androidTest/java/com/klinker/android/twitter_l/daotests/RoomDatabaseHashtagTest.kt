package com.klinker.android.twitter_l.daotests

import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.klinker.android.twitter_l.data.roomdb.HashtagDao
import com.klinker.android.twitter_l.mockentities.MockHashtag
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.notNullValue
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
        val hashtag = MockHashtag()
        val id = hashtagDao.insert(hashtag.hashtag)
        assertThat(id, notNullValue())
        assertThat(database.size, equalTo(1))
    }

}