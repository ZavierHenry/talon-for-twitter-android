package com.klinker.android.twitter_l

import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.klinker.android.twitter_l.data.roomdb.FavoriteUser
import com.klinker.android.twitter_l.data.roomdb.FavoriteUserDao
import com.klinker.android.twitter_l.data.roomdb.User
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.hamcrest.Matchers.*
import org.junit.Rule


@RunWith(AndroidJUnit4ClassRunner::class)
class RoomDatabaseFavoriteUserTest {

    //private lateinit var db : TalonDatabase
    private lateinit var favoriteUserDao: FavoriteUserDao

    private fun makeMockFavoriteUser(
            screenName: String = "chrislhayes",
            name: String? = "Chris Hayes",
            profilePic: String? = "",
            userId: Long? = 1023123L,
            account: Int = 1
    ) : FavoriteUser {
        return FavoriteUser(
                User(screenName, name, profilePic, userId),
                account
        )
    }

    @get:Rule val database = TestDatabase()

    @Before
    fun createFavoriteUserDao() {
        favoriteUserDao = database.database.favoriteUserDao()
    }

    @Test
    @Throws(Exception::class)
    fun insertFavoriteUser() {
        val favoriteUser = makeMockFavoriteUser(account = 1)
        val id = favoriteUserDao.insertFavoriteUser(favoriteUser)
        assertThat(id, notNullValue())
        val favoriteUsers = favoriteUserDao.getFavoriteUsers(1)
        assertThat(favoriteUsers.size, equalTo(1))
        assertThat(favoriteUsers[0].id, equalTo(id))
    }

}