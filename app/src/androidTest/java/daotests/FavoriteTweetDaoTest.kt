package daotests

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.room.Query
import androidx.test.platform.app.InstrumentationRegistry
import com.klinker.android.twitter_l.data.roomdb.entities.FavoriteTweet
import com.klinker.android.twitter_l.data.roomdb.entities.Tweet
import com.klinker.android.twitter_l.data.roomdb.entities.User
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

class FavoriteTweetDaoTest : DaoTest() {

    private val favoriteTweetDao get() = testDatabase.favoriteTweetDao()


    @Test
    fun deleteFavoriteTweetByFavoriteTweet() {

    }


    @Test
    fun deleteFavoriteTweetByTweetIdAndAccount() {
        val account = 1
        val tweetId = 100L

        val user = User(null, 1, "User 1", "user1", "https://twitter.com/img/1/img.png", false)
        val tweet = Tweet(id = tweetId, text = "Sample text 1")
        val favoriteTweet = FavoriteTweet(null, account)
        val id = insertFavoriteTweet(user, tweet, favoriteTweet)?.let { (_, favoriteTweet) -> favoriteTweet.id }

        assertThat("Favorite tweet must be in database to properly run this test", id, not(-1L))

        favoriteTweetDao.deleteFavoriteTweet(tweetId, account)

        queryDatabase("SELECT id FROM favorite_tweets", null).use { cursor ->
            assertThat("Incorrect number of favorite tweets", cursor.count, `is`(0))
        }

    }



    @Test
    fun deleteAllFavoriteTweets() {
        val databaseSize = 3
        val account = 1

        val favoriteTweetIdCount = (0 until databaseSize).count {
            val user = User(null, it.toLong(), "User $it", "user$it", "https://twitter.com/img/$it/img.png", false)
            val tweet = Tweet(id = it.toLong() + 99, text = "Sample text $it")
            val favoriteTweet = FavoriteTweet(null, account)
            insertFavoriteTweet(user, tweet, favoriteTweet) != null
        }

        assertThat("There must be at least 1 favorite tweet in the database to run this test properly", favoriteTweetIdCount, greaterThan(0))
        favoriteTweetDao.deleteAllFavoriteTweets(account)
        queryDatabase("SELECT id FROM favorite_tweets", null).use { cursor ->
            assertThat("Incorrect number of favorite tweets", cursor.count, `is`(0))
        }

    }


    @Test
    fun deleteAllFavoriteTweetsIgnoresOtherAccounts() {
        val databaseSize = 3
        val account = 1
        val otherAccount = account + 1

        val favoriteTweetIdCount = (0 until databaseSize).count {
            val user = User(null, it.toLong(), "User $it", "user$it", "https://twitter.com/img/$it/img.png", false)
            val tweet = Tweet(id = it.toLong() + 99, text = "Sample text $it")
            val favoriteTweet = FavoriteTweet(null, account)
            insertFavoriteTweet(user, tweet, favoriteTweet) != null
        }

        assertThat("At least one favorite tweet must be in the database to run this test properly", favoriteTweetIdCount, greaterThan(0))
        favoriteTweetDao.deleteAllFavoriteTweets(otherAccount)

        queryDatabase("SELECT id FROM favorite_tweets", null).use { cursor ->
            assertThat("Incorrect number of favorite tweets", cursor.count, `is`(favoriteTweetIdCount))
        }

    }


    @Test
    fun getLastIds() {
        val databaseSize = 5
        val idSize = 3
        val account = 1

        val results = (0 until databaseSize).mapNotNull {
            val user = User(null, it.toLong(), "User $it", "user$it", "https://twitter.com/img/$it/img.png", false)
            val tweet = Tweet(id = it.toLong() + 99, text = "Sample text $it")
            val favoriteTweet = FavoriteTweet(null, account)
            insertFavoriteTweet(user, tweet, favoriteTweet)
        }

        assertThat("There must be at least $idSize favorite tweets in the database", results, hasSize(greaterThanOrEqualTo(idSize)))
        val latestIds = favoriteTweetDao.getLatestTweetIds(account, idSize)

        val matchers = results.asSequence()
                .sortedByDescending { (_, favoriteTweet) -> favoriteTweet.tweetId }
                .take(idSize)
                .map { (_, favoriteTweet) -> `is`(favoriteTweet.tweetId) }
                .toList()

        assertThat("Incorrect list of tweet ids", latestIds, contains(matchers))

    }


    @Test
    fun getLastIdsTruncatesIfSizeTooSmall() {
        val databaseSize = 3
        val idSize = 5
        val account = 1

        val results = (0 until databaseSize).mapNotNull{
            val user = User(null, it.toLong(), "User $it", "user$it", "https://twitter.com/img/$it/img.png", false)
            val tweet = Tweet(id = it.toLong(), text = "Sample text $it")
            val favoriteTweet = FavoriteTweet(null, account)
            insertFavoriteTweet(user, tweet, favoriteTweet)
        }

        val latestIds = favoriteTweetDao.getLatestTweetIds(account, idSize)
        val matchers = results.asSequence()
                .sortedByDescending { (_, favoriteTweet) -> favoriteTweet.tweetId }
                .map { (_, favoriteTweet) -> `is`(favoriteTweet.tweetId) }
                .toList()

        assertThat("Incorrect list of tweet ids", latestIds, contains(matchers))

    }


    @Test
    fun getLastIdsIgnoresOtherAccount() {
        val databaseSize = 5
        val idSize = 3
        val account = 1
        val otherAccount = account + 1

        val favoriteTweetIdCount = (0 until databaseSize).count {
            val user = User(null, it.toLong(), "User $it", "user$it", "https://twitter.com/img/$it/img.png", false)
            val tweet = Tweet(id = it.toLong(), text = "Sample text $it")
            val favoriteTweet = FavoriteTweet(null, account)
            insertFavoriteTweet(user, tweet, favoriteTweet) != null
        }

        assertThat("At least one favorite tweet must be in the database to run this test properly", favoriteTweetIdCount, greaterThan(0))
        val latestTweetIds = favoriteTweetDao.getLatestTweetIds(otherAccount, idSize)

        assertThat("Incorrect number of tweet ids", latestTweetIds, empty())

    }


    @Test
    fun trimDatabaseTrimsDatabase() {

        val databaseSize = 5
        val trimSize = 3
        val account = 1

        val favoriteTweetIdCount = (0 until databaseSize).count {
            val user = User(null, it.toLong(), "User $it", "user$it", "https://twitter.com/img/$it/img.png", false)
            val tweet = Tweet(id = it.toLong() + 88, text = "Sample text $it")
            val favoriteTweet = FavoriteTweet(null, account)
            insertFavoriteTweet(user, tweet, favoriteTweet) != null
        }

        assertThat("More than $trimSize favorite tweets must be in the database to test properly", favoriteTweetIdCount, greaterThan(trimSize))
        favoriteTweetDao.trimDatabase(account, trimSize)
        queryDatabase("SELECT id FROM favorite_tweets", null).use { cursor ->
            assertThat("Incorrect number of favorite tweets", cursor.count, `is`(trimSize))
        }

    }


    @Test
    fun trimDatabaseUnchangesSmallDatabase() {
        val databaseSize = 3
        val trimSize = 5
        val account = 1

        val favoriteTweetIdCount = (0 until databaseSize).count {
            val user = User(null, it.toLong(), "User $it", "user$it", "https://twitter.com/img/$it/img.png", false)
            val tweet = Tweet(id = it.toLong() + 88, text = "Sample text $it")
            val favoriteTweet = FavoriteTweet(null, account)
            insertFavoriteTweet(user, tweet, favoriteTweet) != null
        }

        assertThat("At least one favorite tweet must be in the database to run this test properly", favoriteTweetIdCount, greaterThan(0))
        favoriteTweetDao.trimDatabase(account, trimSize)
        queryDatabase("SELECT id FROM favorite_tweets", null).use { cursor ->
            assertThat("Incorrect number of favorite tweets", cursor.count, `is`(databaseSize))
        }

    }


    @Test
    fun trimDatabaseIgnoresOtherAccounts() {
        val databaseSize = 5
        val trimSize = 3
        val account = 1
        val otherAccount = account + 1

        val favoriteTweetIdCount = (0 until databaseSize).count {
            val user = User(null, it.toLong(), "User $it", "user$it", "https://twitter.com/img/$it/img.png", false)
            val tweet = Tweet(id = it.toLong() + 99, text = "Sample text $it")
            val favoriteTweet = FavoriteTweet(null, account)
            insertFavoriteTweet(user, tweet, favoriteTweet) != null
        }

        assertThat("More than $trimSize favorite tweets must be in the database to properly run this test", favoriteTweetIdCount, greaterThan(trimSize))
        favoriteTweetDao.trimDatabase(otherAccount, trimSize)

        queryDatabase("SELECT id FROM favorite_tweets", null).use { cursor ->
            assertThat("Incorrect number of favorite tweets", cursor.count, `is`(favoriteTweetIdCount))
        }

    }


    @Test
    fun trimDatabaseTrimsSmallestTweetIds() {
        val databaseSize = 5
        val trimSize = 3
        val account = 1

        val results = (0 until databaseSize).mapNotNull {
            val user = User(null, it.toLong(), "User $it", "user$it", "https://twitter.com/img/$it/img.png", false)
            val tweet = Tweet(id = it.toLong() + 99, text = "Sample text $it")
            val favoriteTweet = FavoriteTweet(null, account)
            insertFavoriteTweet(user, tweet, favoriteTweet)
        }.sortedByDescending { (_, favoriteTweet) -> favoriteTweet.tweetId }

        assertThat("More than $trimSize favorite tweets must be in the database to run this test properly", results, hasSize(greaterThan(trimSize)))
        favoriteTweetDao.trimDatabase(account, trimSize)

        queryDatabase("SELECT tweet_id FROM favorite_tweets ORDER BY tweet_id DESC", null).use { cursor ->
            assertThat("Problem pointing to first value in the database", cursor.moveToFirst())
            val tweetIds = List(cursor.count) {
                val result = cursor.getLong(0)
                cursor.moveToNext()
                result
            }

            assertThat(
                    "Favorite tweet ids in the database is incorrect",
                    tweetIds,
                    contains(results.asSequence().take(trimSize).map { (_, favoriteTweet) -> `is`(favoriteTweet.tweetId) }.toList()))
        }

    }


    //TODO: write test so favorite tweets rejects duplicates


    @Test
    fun favoriteTweetRestrictsTweetDelete() {
        val user = User(null, 1, "User 1", "user1", "https://twitter.com/img/1/img.png", false)
        val tweet = Tweet(id = 100, text = "Sample text 100")
        val favoriteTweet = FavoriteTweet(null, 1)

        val results = insertFavoriteTweet(user, tweet, favoriteTweet)
        assertThat("Favorite user must be saved to database to properly run this test", results, notNullValue())

        queryDatabase("DELETE FROM tweets WHERE id = ?", arrayOf(tweet.id))
        queryDatabase("SELECT id FROM tweets", null).use { cursor ->
            assertThat("Incorrect number of tweets", cursor.count, `is`(1))
        }

    }


    @After
    fun clearTables() {
        clearTestDatabase()
    }


    private fun setFavoriteTweetContentValues(contentValues: ContentValues, favoriteTweet: FavoriteTweet) {
        with(contentValues) {
            put("id", favoriteTweet.id)
            put("account", favoriteTweet.account)
            put("is_unread", favoriteTweet.isUnread)
            put("tweet_id", favoriteTweet.tweetId)
            put("is_retweeted", favoriteTweet.isRetweeted)
        }
    }

    private fun insertFavoriteTweet(favoriteTweet: FavoriteTweet) : FavoriteTweet? {
        val contentValues = ContentValues()
        setFavoriteTweetContentValues(contentValues, favoriteTweet)
        val id = insertIntoDatabase("favorite_tweets", SQLiteDatabase.CONFLICT_IGNORE, contentValues)
        return if (id != -1L) favoriteTweet.copy(id = id) else null
    }

    private fun insertFavoriteTweet(user: User, tweet: Tweet, favoriteTweet: FavoriteTweet) : Pair<Pair<User, Tweet>, FavoriteTweet>? {

        return insertUser(user)?.let { u ->
            insertTweet(tweet.copy(userId = u.id!!))?.let { t ->
                insertFavoriteTweet(favoriteTweet.copy(tweetId = t.id))?.let { ft ->
                    u to t to ft
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
