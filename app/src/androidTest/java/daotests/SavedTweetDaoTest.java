package daotests;

import android.content.Context;

import com.klinker.android.twitter_l.data.roomdb.TalonDatabase;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import androidx.room.Room;
import androidx.test.platform.app.InstrumentationRegistry;

public class SavedTweetDaoTest extends DaoTest {

    @BeforeClass
    public static void initDatabase() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        testDatabase = Room.inMemoryDatabaseBuilder(context, TalonDatabase.class)
                .build();
    }

    @Test
    public void insertSavedTweet() {

    }

    @Test
    public void insertSavedTweetWithConflict() {

    }

    @Test
    public void insertSavedTweetSameIdDifferentAccount() {

    }

    @Test
    public void deleteSavedTweetObject() {

    }

    @Test
    public void deleteSavedTweetId() {

    }

    @Test
    public void getSavedTweetsCursor() {

    }

    @Test
    public void isTweetSaved() {

    }

    @After
    public void clearTables() {
        testDatabase.clearAllTables();
    }


    @AfterClass
    public void closeDatabase() {
        testDatabase.close();
        testDatabase = null;
    }
}
