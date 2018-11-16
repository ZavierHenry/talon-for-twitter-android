package daotests

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase

import com.klinker.android.twitter_l.data.roomdb.TalonDatabase

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.klinker.android.twitter_l.data.roomdb.entities.QueuedTweet
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.*

class QueuedTweetDaoTest : DaoTest() {

    private val queuedTweetDao get() = testDatabase.queuedTweetDao()

    @Test
    fun insertQueuedTweet() {

        val text = "This is a sample queued tweet text"
        val account = 1

        val queuedTweet = QueuedTweet(text, account)
        val id = queuedTweetDao.insertQueuedTweet(queuedTweet)
        assertThat("Queued tweet did not insert into the database", id, not(-1L))

    }


    @Test
    fun insertQueuedTweetDuplicateText() {

        var id : Long

        val text = "This is a sample queued tweet text"
        val account = 1
        val queuedTweet = QueuedTweet(text, account)

        id = queuedTweetDao.insertQueuedTweet(queuedTweet)
        assertThat("Did not insert initial value into database properly", id, not(-1L))

        id = queuedTweetDao.insertQueuedTweet(queuedTweet)
        assertThat("Did not insert duplicate text value into database properly", id, not(-1L))

    }


    @Test
    fun deleteQueuedTweet() {
        val contentValues = ContentValues()
        val text = "This is sample text for this queued tweet"
        val account = 1

        contentValues.put("text", text)
        contentValues.put("account", account)

        val id = insertIntoDatabase("queued_tweets", SQLiteDatabase.CONFLICT_ABORT, contentValues)
        assertThat("Values did not insert into the database properly", id, not(-1L))

        val queuedTweet = QueuedTweet(id, text, account)
        queuedTweetDao.deleteQueuedTweet(queuedTweet)

        val cursor = queryDatabase("SELECT * FROM queued_tweets", null)
        assertThat("Value did not delete from the database", cursor.count, `is`(0))
        cursor.close()

    }

    @Test
    fun getQueuedTweets() {
        val contentValues = ContentValues()
        val testAccountNumber = 1

        contentValues.put("account", 1)

        beginTransaction()

        val firstAccountIds = (0..2).map {
            contentValues.put("text", "This is a sample text for tweet $it")
            insertIntoDatabase("queued_tweets", SQLiteDatabase.CONFLICT_ABORT, contentValues)
        }.count { it != -1L }

        contentValues.put("account", 2)

        (0..2).forEach {
            contentValues.put("text", "This is a sample text for tweet $it")
            insertIntoDatabase("queued_tweets", SQLiteDatabase.CONFLICT_ABORT, contentValues)
        }

        endSuccessfulTransaction()

        val queuedTweets = queuedTweetDao.getQueuedTweets(testAccountNumber)
        assertThat("Did not get the correct number of values", queuedTweets, hasSize(firstAccountIds))
        assertThat("A queued tweet has the wrong account number", queuedTweets.all{ it.account == testAccountNumber})

    }

    @Test
    fun getQueuedTweetsEmptyList() {
        val account = 1
        val contentValues = ContentValues()
        contentValues.put("account", account)

        beginTransaction()

        val numberInsertedIds = (0..5).map {
            contentValues.put("text", "This is a sample text for queued tweet $it")
            insertIntoDatabase("queued_tweets", SQLiteDatabase.CONFLICT_ABORT, contentValues)
        }.count { it != -1L }

        endSuccessfulTransaction()

        assertThat("At least one value must be inserted to properly test this", numberInsertedIds, greaterThan(0))
        val queriedTweets = queuedTweetDao.getQueuedTweets(account + 1)
        assertThat("Incorrect number of queued tweets returned", queriedTweets, emptyCollectionOf(QueuedTweet::class.java))

    }

    @Test
    fun deleteAllQueuedTweets() {
        val contentValues = ContentValues()
        contentValues.put("account", 1)
        contentValues.put("text", "This is a sample queued text")

        beginTransaction()

        val id1 = insertIntoDatabase("queued_tweets", SQLiteDatabase.CONFLICT_ABORT, contentValues)
        contentValues.put("account", 2)
        val id2 = insertIntoDatabase("queued_tweets", SQLiteDatabase.CONFLICT_ABORT, contentValues)

        endSuccessfulTransaction()

        assertThat("Problem inserting test value 1 into database", id1, not(-1L))
        assertThat("Problem inserting test value 2 into database", id2, not(-1L))

        queuedTweetDao.deleteAllQueuedTweets()

        val cursor = queryDatabase("SELECT * FROM queued_tweets", null)
        assertThat("Database values were not deleted properly", cursor.count, `is`(0))
        cursor.close()

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
