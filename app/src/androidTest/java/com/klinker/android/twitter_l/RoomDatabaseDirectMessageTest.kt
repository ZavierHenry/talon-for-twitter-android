package com.klinker.android.twitter_l

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.klinker.android.twitter_l.data.roomdb.DirectMessageDao
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class RoomDatabaseDirectMessageTest {

    private lateinit var directMessageDao: DirectMessageDao

    @get:Rule val database = TestDatabase("direct_messages")

    @Before
    fun getDirectMessageDao() {
        directMessageDao = database.database.directMessageDao()
    }

}