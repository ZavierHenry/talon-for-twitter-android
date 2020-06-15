package com.klinker.android.twitter_l

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.klinker.android.twitter_l.data.roomdb.FavoriteTweetDao
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class RoomDatabaseFavoriteTweetTest {

    private lateinit var favoriteTweetDao: FavoriteTweetDao

    @get:Rule val database = TestDatabase("favorite_tweets")

    @Before
    fun getFavoriteTweetDao() {
        favoriteTweetDao = database.database.favoriteTweetDao()
    }

}