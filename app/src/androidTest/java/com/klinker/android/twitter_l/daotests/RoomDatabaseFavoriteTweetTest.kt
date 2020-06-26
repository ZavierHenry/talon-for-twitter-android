package com.klinker.android.twitter_l.daotests

import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.klinker.android.twitter_l.mockentities.MockFavoriteTweet
import com.klinker.android.twitter_l.data.roomdb.FavoriteTweetDao
import com.klinker.android.twitter_l.mockentities.matchers.EntityValidIdMatcher.Companion.hasInvalidId
import com.klinker.android.twitter_l.mockentities.matchers.EntityValidIdMatcher.Companion.hasValidId
import org.hamcrest.CoreMatchers.*
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
        val favoriteTweet = favoriteTweetDao.insert(MockFavoriteTweet(1).favoriteTweet)
        assertThat(MockFavoriteTweet(favoriteTweet), hasValidId())
        assertThat(database.size, equalTo(1))
    }


    @Test
    @Throws(Exception::class)
    fun testInsertFavoriteTweet_DuplicateId() {
        val favoriteTweet = MockFavoriteTweet(1)
        val id = database.insertIntoDatabase(favoriteTweet)
        assertThat(database.size, equalTo(1))
        assertThat(id, not(equalTo(-1L)))

        val duplicateFavoriteTweet = favoriteTweet.favoriteTweet.copy(id = id)
        val duplicateInsertedTweet = favoriteTweetDao.insert(duplicateFavoriteTweet)
        assertThat(MockFavoriteTweet(duplicateInsertedTweet), hasInvalidId())
        assertThat(database.size, equalTo(1))
    }

}