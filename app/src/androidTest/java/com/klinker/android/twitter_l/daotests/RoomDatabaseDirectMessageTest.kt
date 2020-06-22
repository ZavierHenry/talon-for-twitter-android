package com.klinker.android.twitter_l.daotests

import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.klinker.android.twitter_l.mockentities.MockDirectMessage
import com.klinker.android.twitter_l.data.roomdb.DirectMessageDao
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
        val id = directMessageDao.insert(directMessage.directMessage)
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

        directMessageDao.delete(directMessage.directMessage.copy(id = id))
        assertThat("Direct message was not properly deleted from the database", database.size, equalTo(0))
    }

    @Test
    @Throws(Exception::class)
    fun testTrimDatabase_NoTrim() {
        val directMessages = List(10) {
            MockDirectMessage(1, twitterId = it.toLong() + 1)
        }

        directMessages.forEach { database.insertIntoDatabase(it) }
        assertThat("Problem setting up initial entities in database", database.size, equalTo(directMessages.size))
        directMessageDao.trimDatabase(1, 20)
        assertThat("Number of entries in database changed", database.size, equalTo(directMessages.size))
    }

    @Test
    @Throws(Exception::class)
    fun testTrimDatabase_NoTrim_RespectAccount() {
        val directMessages = List(10) {
            MockDirectMessage(1, twitterId = it.toLong() + 1)
        }

        val secondAccountDirectMessage = MockDirectMessage(2, 300L)

        directMessages.forEach { database.insertIntoDatabase(it) }
        database.insertIntoDatabase(secondAccountDirectMessage)

        assertThat("Problem setting up initial entries in database", database.size, equalTo(directMessages.size + 1))
        directMessageDao.trimDatabase(1, directMessages.size)
        assertThat("Trim database does not respect account of entity", database.size, equalTo(directMessages.size + 1))

    }




}