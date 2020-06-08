package com.klinker.android.twitter_l

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.klinker.android.twitter_l.data.roomdb.Follower
import com.klinker.android.twitter_l.data.roomdb.FollowerDao
import com.klinker.android.twitter_l.data.roomdb.TalonDatabase
import com.klinker.android.twitter_l.data.roomdb.User
import org.hamcrest.Matchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


@RunWith(AndroidJUnit4ClassRunner::class)
class RoomDatabaseFollowerTest {
    private lateinit var followerDao : FollowerDao
    private lateinit var db : TalonDatabase

    @Before
    fun createTalonDatabase() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, TalonDatabase::class.java).build()
        followerDao = db.followerDao()
    }

    @After
    @Throws(IOException::class)
    fun closeTalonDatabase() {
        db.close()
    }


    @Test
    @Throws(Exception::class)
    fun insertFollower() {
        val follower = Follower(
                User("chrislhayes", "Chris Hayes", "", 1023482L),
                1
        )

        val id = followerDao.insertFollower(follower)
        val followers = followerDao.getFollowers(1)
        assertThat(followers.size, equalTo(1))
        assertThat(followers[0].id, equalTo(id))
    }


}