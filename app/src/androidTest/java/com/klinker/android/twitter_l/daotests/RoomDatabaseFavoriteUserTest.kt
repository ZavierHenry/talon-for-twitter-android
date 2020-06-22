package com.klinker.android.twitter_l.daotests

import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.klinker.android.twitter_l.data.roomdb.FavoriteUser
import com.klinker.android.twitter_l.data.roomdb.FavoriteUserDao
import com.klinker.android.twitter_l.data.roomdb.User
import com.klinker.android.twitter_l.mockentities.MockFavoriteUser
import com.klinker.android.twitter_l.mockentities.MockUtilities
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

    @get:Rule val database = TestDatabase("favorite_users")

    @Before
    fun createFavoriteUserDao() {
        favoriteUserDao = database.database.favoriteUserDao()
    }

    @Test
    @Throws(Exception::class)
    fun insertFavoriteUser() {
        val favoriteUser = makeMockFavoriteUser(account = 1)
        val id = favoriteUserDao.insert(favoriteUser)
        assertThat("Did not return a valid id. Most likely a problem inserting entity into database", id, notNullValue())
        assertThat("Incorrect number of entries in database", database.size, equalTo(1))
    }

    @Test
    @Throws(Exception::class)
    fun updateFavoriteUser() {
        val image1 = "Image_1.jpg"
        val image2 = "Image_2.jpg"

        val favoriteUser = MockFavoriteUser(1, user = MockUtilities.makeMockUser(profilePic = image1))
        val id = database.insertIntoDatabase(favoriteUser)
        assertThat("Problem setting up initial entity in database", id, notNullValue())

        val newFavoriteUser = MockFavoriteUser(1, user = MockUtilities.makeMockUser(profilePic = image2), id = id)
        favoriteUserDao.update(newFavoriteUser.favoriteUser)
        assertThat("Update somehow changes the number of entities in the database", database.size, equalTo(1))

        val profilePic = database.queryFromDatabase("SELECT profile_pic FROM favorite_users WHERE id = ?", arrayOf(id as Any)).use { cursor ->
            cursor.moveToFirst()
            cursor.getString(0)
        }

        assertThat("Profile pic did not update properly", profilePic, equalTo(image2))

    }
}