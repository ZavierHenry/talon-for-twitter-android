package daotests

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase

import com.klinker.android.twitter_l.data.roomdb.TalonDatabase

import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.klinker.android.twitter_l.data.roomdb.entities.HomeTweet
import com.klinker.android.twitter_l.data.roomdb.entities.Tweet
import com.klinker.android.twitter_l.data.roomdb.entities.User
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.greaterThan

class HomeTweetDaoTest : DaoTest() {

    private val homeTweetDao get() = testDatabase.homeTweetDao()


    @Test
    fun trimDatabaseTrimsDatabase() {
        val databaseSize = 5
        val trimSize = 3
        val account = 1

        val homeTweetCount = (0 until databaseSize).count {
            val user = User(null, it.toLong(), "User $it", "user$it", "https://twitter.com/img/$it/img.png", false)
            val tweet = Tweet(it.toLong() + 1, text = "Sample text $it")
            val homeTweet = HomeTweet(null, account = account)
            insertHomeTweet(user, tweet, homeTweet) != null
        }

        assertThat("There must be more than $trimSize home tweets in the database to properly run this test", homeTweetCount, greaterThan(trimSize))

        homeTweetDao.trimDatabase(account, trimSize)
        queryDatabase("SELECT id FROM home_tweets", null).use { cursor ->
            assertThat("Incorrect number of home tweets", cursor.count, `is`(trimSize))
        }

    }

    @Test
    fun trimDatabaseLeavesSmallDatabaseUnchanged() {
        val databaseSize = 3
        val trimSize = 5
        val account = 1

    }


    @Test
    fun trimDatabaseIgnoresOtherAccounts() {

    }


    @Test
    fun homeTweetRestrictsUserDelete() {

    }

    @Test
    fun homeTweetRestrictsTweetDelete() {

    }


    @After
    fun clearTables() {
        clearTestDatabase()
    }

    private fun setHomeTweetContentValues(contentValues: ContentValues, homeTweet: HomeTweet) {
        with(contentValues) {
            put("id", homeTweet.id)
            put("account", homeTweet.account)
            put("is_unread", homeTweet.isUnread)
            put("tweet_id", homeTweet.tweetId)
            put("is_current_pos", homeTweet.isCurrentPos)
            put("is_liked", homeTweet.isLiked)
            put("is_retweeted", homeTweet.isRetweeted)
        }
    }

    private fun insertHomeTweet(homeTweet: HomeTweet): HomeTweet? {
        val contentValues = ContentValues()
        setHomeTweetContentValues(contentValues, homeTweet)
        val id = insertIntoDatabase("home_tweets", SQLiteDatabase.CONFLICT_IGNORE, contentValues)
        return if (id != -1L) homeTweet.copy(id = id) else null
    }

    private fun insertHomeTweet(user: User, tweet: Tweet, homeTweet: HomeTweet) : Pair<Pair<User, Tweet>, HomeTweet>? {
        return insertUser(user)?.let { u ->
            insertTweet(tweet.copy(userId = u.id!!))?.let { t ->
                insertHomeTweet(homeTweet.copy(tweetId = t.id))?.let { ht ->
                    u to t to ht
                }
            }
        }

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
