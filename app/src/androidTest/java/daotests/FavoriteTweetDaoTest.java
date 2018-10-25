package daotests;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class FavoriteTweetDaoTest extends DaoTest {


    @BeforeClass
    public static void initDatabase() {

    }

    @Test
    public void insertFavoriteTweet() {

    }

    @Test
    public void insertFavoriteTweetFails_ConflictTest() {

    }

    @Test
    public void insertFavoriteTweet_DuplicateTest() {

    }


    @Test
    public void insertFavoriteTweets() {

    }

    @Test
    public void insertFavoriteTweets_WithConflicts() {

    }

    @Test
    public void deleteFavoriteTweet_WithFavoriteTweet() {

    }

    @Test
    public void deleteFavoriteTweet_WithTweetId() {

    }

    @Test
    public void deleteAllFavoriteTweets() {

    }

    @Test
    public void getFavoriteTweetsCursor() {

    }

    @Test
    public void getLatestFavoriteTweetIds(int account) {

    }


    @Test
    public void tweetExists(long tweetId, int account) {

    }

    @Test
    public void trimDatabaseTrimsDatabase(int account, int trimSize) {

    }

    @After
    public void clearTables() {
        testDatabase.clearAllTables();
    }

    @AfterClass
    public static void closeDatabase() {
        testDatabase.close();
        testDatabase = null;
    }
}
