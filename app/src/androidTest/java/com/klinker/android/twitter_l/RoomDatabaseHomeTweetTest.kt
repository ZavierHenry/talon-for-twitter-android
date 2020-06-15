package com.klinker.android.twitter_l

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.klinker.android.twitter_l.data.roomdb.HomeTweetDao
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class RoomDatabaseHomeTweetTest {

    private lateinit var homeTweetDao: HomeTweetDao

    @get:Rule val database = TestDatabase("home_tweets")


    @Before
    fun getHomeTweetDao() {
        homeTweetDao = database.database.homeTweetDao()
    }
    
}