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
import org.hamcrest.Matchers.*

class ScheduledTweetDaoTest : DaoTest() {

    private val scheduledTweetDao get() = testDatabase.scheduledTweetDao()

    @Test
    fun insertScheduledTweet() {
        val alarmId = 1L
        val text = "This is a test message"
        val time = 100000L
        val account = 1

        val scheduledTweet = ScheduledTweet(alarmId, text, time, account)
        val id = scheduledTweetDao.insertScheduledTweet(scheduledTweet)
        assertThat("Scheduled tweet did not insert into the database", id, not(-1L))

    }


    @Test
    fun insertScheduledTweetDuplicateText() {
        val alarmId = 1L
        val alarmId2 = 2L

        val text = "This is a sample text message"
        val time = 10000L
        val account = 1

        val scheduledTweet = ScheduledTweet(alarmId, text, time, account)
        val scheduledTweet2 = ScheduledTweet(alarmId2, text, time, account)

        scheduledTweetDao.insertScheduledTweet(scheduledTweet)
        scheduledTweetDao.insertScheduledTweet(scheduledTweet2)

        queryDatabase("SELECT * FROM scheduled_tweets", null).use { cursor ->
            assertThat("Database doesn't allow duplicate scheduled tweet text", cursor.count, `is`(2))
        }

    }


    @Test
    fun deleteScheduledTweet() {
        val alarmId = 75L
        val text = "This is a sample text message"
        val time = 110000L
        val account = 1

        val scheduledTweet = ScheduledTweet(alarmId, text, time, account)
        val contentValues = ContentValues()
        contentValues.put("alarm_id", alarmId)
        contentValues.put("text", text)
        contentValues.put("account", account)
        contentValues.put("time", time)

        val id = insertIntoDatabase("scheduled_tweets", SQLiteDatabase.CONFLICT_IGNORE, contentValues)
        assertThat("Value got inserted into the database", id, not(-1L))

        scheduledTweetDao.deleteScheduledTweet(scheduledTweet)

        val cursor = queryDatabase("SELECT alarm_id FROM scheduled_tweets WHERE alarm_id = ?", arrayOf(scheduledTweet.alarmId))
        assertThat("Scheduled tweet is not deleted from the database", cursor.count, `is`(0))
        cursor.close()
    }


    @Test
    fun getScheduledTweets() {

        val account = 1
        val time = 3000

        val contentValues = ContentValues()
        contentValues.put("account", account)
        contentValues.put("time", time)
        contentValues.put("text", "This is a scheduled tweet test string")

        val insertedCount = asTransaction {

            val insertedCount = (1..10).count {
                contentValues.put("alarm_id", it)
                insertIntoDatabase("scheduled_tweets", SQLiteDatabase.CONFLICT_IGNORE, contentValues) != -1L
            }

            contentValues.put("account", account + 1)
            contentValues.put("alarm_id", insertedCount + 55)
            val id = insertIntoDatabase("scheduled_tweets", SQLiteDatabase.CONFLICT_IGNORE, contentValues)
            assertThat("At least one of a not queried account must be inserted to properly test this", id, not(-1L))

            insertedCount

        }

        val scheduledTweets = scheduledTweetDao.getScheduledTweets(account)
        assertThat("Querying for a list of scheduled tweets did not return tweets", scheduledTweets, hasSize(insertedCount))
        assertThat("Filtered element is not in there", scheduledTweets.all { it.account == account })
    }


    @Test
    fun getScheduledTweetsEmptyList() {

        val account = 1
        val time = 113000L

        val contentValues = ContentValues()
        contentValues.put("time", time)
        contentValues.put("account", account)


        val insertedCount = asTransaction {

            (0L..9L).any {
                val alarmId = it
                val text = "This is the sample text for tweet $it"
                contentValues.put("alarm_id", alarmId)
                contentValues.put("text", text)
                insertIntoDatabase("scheduled_tweets", SQLiteDatabase.CONFLICT_IGNORE, contentValues) != -1L
            }

        }

        assertThat("At least one value must be in database to properly test this", insertedCount)

        val scheduledTweets = scheduledTweetDao.getScheduledTweets(account + 1)
        assertThat("Database returned value when it shouldn't have", scheduledTweets, emptyCollectionOf(ScheduledTweet::class.java))

    }


    @Test
    fun getEarliestScheduledTweet() {

        val requestedTime = 10000L

        val account = 1
        val text = "This is sample text for this scheduled tweet"
        val time = 12000L
        val alarmId = 1
        val contentValues = ContentValues()

        contentValues.put("account", account)
        contentValues.put("text", text)
        contentValues.put("alarm_id", alarmId)
        contentValues.put("time", time)
        val id = insertIntoDatabase("scheduled_tweets", SQLiteDatabase.CONFLICT_ABORT, contentValues)
        assertThat("Problem putting test value in the database", id, not(-1L))

        val scheduledTweet = scheduledTweetDao.getEarliestScheduledTweet(requestedTime)
        assertThat("Requested database value did not return", scheduledTweet, notNullValue())

    }


    @Test
    fun getNoScheduledTweetIfTimeisTooLate() {

        val requestedTime = 10000L

        val account = 1
        val text = "This is a sample text"
        val time = 5000L
        val alarmId = 1
        val contentValues = ContentValues()

        contentValues.put("alarm_id", alarmId)
        contentValues.put("time", time)
        contentValues.put("text", text)
        contentValues.put("account", account)

        val id = insertIntoDatabase("scheduled_tweets", SQLiteDatabase.CONFLICT_ABORT, contentValues)
        assertThat("Test value did not enter into database", id, not(-1L))
        val scheduledTweet = scheduledTweetDao.getEarliestScheduledTweet(requestedTime)
        assertThat("Returned a scheduled tweet when it shouldn't have", scheduledTweet, nullValue())
    }


    @After
    fun clearTables() {
        clearTestDatabase()
    }

    companion object {

        @BeforeClass
        @JvmStatic fun initDatabase() {
            DaoTest.initTestDatabase()
        }

        @AfterClass
        @JvmStatic fun closeDatabase() {
            DaoTest.closeTestDatabase()
        }
    }
}
