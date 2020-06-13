package com.klinker.android.twitter_l

import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.klinker.android.twitter_l.data.roomdb.Follower
import com.klinker.android.twitter_l.data.roomdb.FollowerDao
import com.klinker.android.twitter_l.data.roomdb.User
import org.hamcrest.Matchers.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class RoomDatabaseFollowerTest {

    private lateinit var followerDao : FollowerDao
    
    private fun makeMockFollower(
            screenName: String = "chrislhayes", 
            name: String? = "Chris Hayes",
            profilePic: String? = "",
            userId: Long? = 1023123L,
            account: Int = 1
    ) : Follower {
        return Follower(User(screenName, name, profilePic, userId), account)
    }

    @get:Rule val database = TestDatabase()

    @Before
    fun createFollowerDao() {
        followerDao = database.database.followerDao()
    }
    
    @Test
    @Throws(Exception::class)
    fun insertFollower() {
        val follower = makeMockFollower(account = 1)
        val id = followerDao.insertFollower(follower)
        val followers = followerDao.getFollowers(1)
        assertThat(followers.size, equalTo(1))
        assertThat(followers[0].id, equalTo(id))
    }


    @Test
    @Throws(Exception::class)
    fun deleteFollower() {
        val follower = makeMockFollower(account = 1)
        val id = followerDao.insertFollower(follower)
        assertThat(id, notNullValue())
        followerDao.deleteFollower(follower.copy(id = id))
        assertThat(followerDao.getFollowers(1).size, equalTo(0))
    }

    @Test
    @Throws(Exception::class)
    fun getFollowersFilterAccount() {
        val followers = List(10) {
            makeMockFollower("chrislhayes${it}", "Chris Hayes ${it}", "image ${it}", it.toLong(), if (it > 5) 2 else 1)
        }
        val insertedFollowers = followers.mapNotNull {
            follower -> followerDao.insertFollower(follower)?.let { id -> follower.copy(id = id) }
        }
        assertThat(insertedFollowers.size, equalTo(followers.size))
        val databaseFollowers = followerDao.getFollowers(2)
        assertThat(
                databaseFollowers.map { it.id }.sortedBy { it },
                contains(*insertedFollowers.filter { it.account == 2}.map { it.id }.sortedBy { it }.toTypedArray())
        )
    }

}