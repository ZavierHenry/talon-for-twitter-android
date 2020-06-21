package com.klinker.android.twitter_l.daotests

import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.klinker.android.twitter_l.mockentities.MockFavoriteTweet
import com.klinker.android.twitter_l.data.roomdb.FavoriteTweetDao
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class RoomDatabaseFavoriteTweetTest {

    private lateinit var favoriteTweetDao: FavoriteTweetDao

    @get:Rule val database = TestDatabase("favorite_tweets")

    @Before
    fun getFavoriteTweetDao() {
        favoriteTweetDao = database.database.favoriteTweetDao()
    }

    @Test
    @Throws(Exception::class)
    fun testInsertFavoriteTweet() {
        val favoriteTweet = MockFavoriteTweet(1)
        val id = favoriteTweetDao.insert(favoriteTweet.favoriteTweet)
        assertThat(id, notNullValue())
        assertThat(database.size, equalTo(1))
    }

}