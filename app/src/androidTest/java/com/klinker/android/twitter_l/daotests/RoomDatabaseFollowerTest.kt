package com.klinker.android.twitter_l.daotests

import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.klinker.android.twitter_l.data.roomdb.Follower
import com.klinker.android.twitter_l.data.roomdb.FollowerDao
import com.klinker.android.twitter_l.data.roomdb.User
import com.klinker.android.twitter_l.mockentities.MockFollower
import com.klinker.android.twitter_l.mockentities.MockUtilities
import org.hamcrest.Matchers.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class RoomDatabaseFollowerTest {

    private lateinit var followerDao : FollowerDao

    private fun makeExampleFollower(
            account: Int,
            screenName: String = "chrislhayes",
            name: String? = "Chris Hayes",
            profilePic: String? = "",
            userId: Long? = 1023123L
    ) : Follower {
        return Follower(User(screenName, name, profilePic, userId), account)
    }

    @get:Rule val database = TestDatabase("followers")

    @Before
    fun createFollowerDao() {
        followerDao = database.database.followerDao()
    }
    
    @Test
    @Throws(Exception::class)
    fun insertFollower() {
        val follower = makeExampleFollower(1)
        val id = followerDao.insert(follower)
        assertThat(id, notNullValue())
        assertThat(database.size, equalTo(1))
    }


    @Test
    @Throws(Exception::class)
    fun deleteFollower() {
        val follower = MockFollower(1)
        val id = database.insertIntoDatabase(follower)
        assertThat(id, notNullValue())
        assertThat(database.size, equalTo(1))
        followerDao.delete(follower.follower.copy(id = id))
        assertThat(database.size, equalTo(0))
    }

    @Test
    @Throws(Exception::class)
    fun getFollowersFilterAccount() {

        val followers = List(10) {
            MockFollower(if (it > 5) 2 else 1, MockUtilities.makeMockUser(screenName = "chrislhayes$it", name = "Chris Hayes $it", userId = it.toLong()))
        }

        val insertedFollowers = followers.mapNotNull { follower ->
            database.insertIntoDatabase(follower)?.let { id -> follower.follower.copy(id = id) }
        }

        assertThat(insertedFollowers.size, equalTo(followers.size))
        val databaseFollowers = followerDao.getFollowers(2)
        assertThat(
                databaseFollowers.map { it.id }.sortedBy { it },
                contains(*insertedFollowers.filter { it.account == 2}.map { it.id }.sortedBy { it }.toTypedArray())
        )
    }

}