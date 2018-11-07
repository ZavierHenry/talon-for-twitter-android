package daotests

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.strictmode.SqliteObjectLeakedViolation

import com.klinker.android.twitter_l.data.roomdb.TalonDatabase
import com.klinker.android.twitter_l.data.roomdb.entities.ScheduledTweet

import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.not
import org.hamcrest.Matchers.notNullValue

class ScheduledTweetDaoTest : DaoTest() {

    @Test
    fun insertScheduledTweet() {
        val scheduledTweet = ScheduledTweet(1, "This is a test message", 100000, 1)
        DaoTest.testDatabase!!.scheduledTweetDao().insertScheduledTweet(scheduledTweet)

        val cursor = DaoTest.testDatabase!!.query("SELECT alarm_id FROM scheduled_tweets WHERE alarm_id = ?", arrayOf(java.lang.Long.toString(scheduledTweet.alarmId.toLong())))
        assertThat("Scheduled tweet is not inserted in the database", cursor.count, `is`(1))
    }

    @Test
    fun deleteScheduledTweet() {
        val scheduledTweet = ScheduledTweet(76, "This is a test message", 130000, 1)
        val contentValues = ContentValues()
        contentValues.put("alarm_id", scheduledTweet.alarmId)
        contentValues.put("text", scheduledTweet.text)
        contentValues.put("account", scheduledTweet.account)
        contentValues.put("time", scheduledTweet.time)

        val id = DaoTest.testDatabase!!.openHelper.writableDatabase.insert("scheduled_tweets", SQLiteDatabase.CONFLICT_IGNORE, contentValues)
        assertThat("Initial setup for deleteScheduledTweet test failed", id, not<Number>(-1))


        DaoTest.testDatabase!!.scheduledTweetDao().deleteScheduledTweet(scheduledTweet)
        val cursor = DaoTest.testDatabase!!.query("SELECT alarm_id FROM scheduled_tweets WHERE alarm_id = ?", arrayOf(java.lang.Long.toString(scheduledTweet.alarmId.toLong())))
        assertThat("Scheduled tweet is not deleted from the database", cursor.count, `is`(0))
    }


    @Test
    fun getScheduledTweets() {

        val account = 1
        val contentValues = ContentValues()
        contentValues.put("time", 3000)
        contentValues.put("text", "This is a scheduled tweet test string")

        for (i in 0..399) {
            contentValues.put("account", if (i < 300) 1 else 2)
            contentValues.put("alarm_id", i)
            DaoTest.testDatabase!!.openHelper.writableDatabase.insert("scheduled_tweets", SQLiteDatabase.CONFLICT_IGNORE, contentValues)
        }

        //assert some setup assertions

        val scheduledTweets = DaoTest.testDatabase!!.scheduledTweetDao().getScheduledTweets(1)
        assertThat("Querying for a list of scheduled tweets did not return tweets", scheduledTweets, notNullValue())

    }


    @Test
    fun getEarliestScheduledTweet() {

        val time: Long = 10000
        val scheduledTweet = DaoTest.testDatabase!!.scheduledTweetDao().getEarliestScheduledTweets(time)
    }


    @Test
    fun getNoScheduledTweetIfTimeisTooEarly() {

        val time: Long = 10000
        val scheduledTweet = DaoTest.testDatabase!!.scheduledTweetDao().getEarliestScheduledTweets(time)
    }


    @After
    fun clearTables() {
        DaoTest.clearTestDatabase()
    }

    companion object {

        @BeforeClass
        fun initDatabase() {
            DaoTest.initTestDatabase()
        }

        @AfterClass
        fun closeDatabase() {
            DaoTest.closeTestDatabase()
        }
    }
}
