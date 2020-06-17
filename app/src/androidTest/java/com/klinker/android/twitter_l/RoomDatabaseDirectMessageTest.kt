package com.klinker.android.twitter_l

import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.klinker.android.twitter_l.data.roomdb.DirectMessage
import com.klinker.android.twitter_l.data.roomdb.DirectMessageDao
import com.klinker.android.twitter_l.data.roomdb.User
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class RoomDatabaseDirectMessageTest {

    private lateinit var directMessageDao: DirectMessageDao

    @get:Rule val database = TestDatabase("direct_messages")

    @Before
    fun getDirectMessageDao() {
        directMessageDao = database.database.directMessageDao()
    }

    @Test
    @Throws(Exception::class)
    fun testInsertDirectMessage() {
        val directMessage = MockDirectMessage(1)
        val id = directMessageDao.insertDirectMessage(directMessage.directMessage)
        assertThat(id, notNullValue())
        assertThat(database.size, equalTo(1))
    }

    @Test
    @Throws(Exception::class)
    fun testDeleteDirectMessage() {
        val directMessage = MockDirectMessage(1)
        val id = database.insertIntoDatabase(directMessage)
        assertThat(id, notNullValue())
        assertThat(database.size, equalTo(1))

        directMessageDao.deleteDirectMessage(directMessage.directMessage.copy(id = id))
        assertThat(database.size, equalTo(0))
    }

}