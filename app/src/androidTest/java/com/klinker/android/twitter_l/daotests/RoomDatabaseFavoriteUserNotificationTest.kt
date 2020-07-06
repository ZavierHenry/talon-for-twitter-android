package com.klinker.android.twitter_l.daotests

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.klinker.android.twitter_l.data.roomdb.FavoriteUserNotificationDao
import com.klinker.android.twitter_l.mockentities.MockFavoriteTweet
import com.klinker.android.twitter_l.mockentities.MockFavoriteUserNotification
import com.klinker.android.twitter_l.mockentities.matchers.EntityValidIdMatcher.Companion.hasValidId
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class RoomDatabaseFavoriteUserNotificationTest {
    private lateinit var favoriteUserNotificationDao: FavoriteUserNotificationDao

    @get:Rule val database = TestDatabase("favorite_user_notifications")

    @Before
    fun getFavoriteUserNotificationDao() {
        favoriteUserNotificationDao = database.database.favoriteUserNotificationDao()
    }

    @Test
    @Throws(Exception::class)
    fun testInsertFavoriteUserNotification() {
        val favoriteUserNotification = favoriteUserNotificationDao.insert(
                MockFavoriteUserNotification().favoriteUserNotification
        )
        assertThat(MockFavoriteUserNotification(favoriteUserNotification), hasValidId())
        assertThat(database.size, equalTo(1))
    }


}