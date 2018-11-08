package daotests

import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

class FavoriteTweetDaoTest : DaoTest() {

    private val favoriteTweetDao get() = testDatabase.favoriteTweetDao()

    @Test
    fun insertFavoriteTweet() {

    }

    @Test
    fun insertFavoriteTweetFails_ConflictTest() {

    }

    @Test
    fun insertFavoriteTweet_DuplicateTest() {

    }


    @Test
    fun insertFavoriteTweets() {

    }

    @Test
    fun insertFavoriteTweets_WithConflicts() {

    }

    @Test
    fun deleteFavoriteTweet_WithFavoriteTweet() {

    }

    @Test
    fun deleteFavoriteTweet_WithTweetId() {

    }

    @Test
    fun deleteAllFavoriteTweets() {

    }

    @Test
    fun getFavoriteTweetsCursor() {

    }

    @Test
    fun getLatestFavoriteTweetIds(account: Int) {

    }


    @Test
    fun tweetExists(tweetId: Long, account: Int) {

    }

    @Test
    fun trimDatabaseTrimsDatabase(account: Int, trimSize: Int) {

    }

    @After
    fun clearTables() {
        clearTestDatabase()
    }

    companion object {


        @BeforeClass
        fun initDatabase() {

        }

        @AfterClass
        fun closeDatabase() {
            DaoTest.closeTestDatabase()
        }
    }
}
