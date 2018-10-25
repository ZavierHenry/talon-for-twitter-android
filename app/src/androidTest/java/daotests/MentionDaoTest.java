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

public class MentionDaoTest extends DaoTest {

    @BeforeClass
    public static void initDatabase() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        testDatabase = Room.inMemoryDatabaseBuilder(context, TalonDatabase.class)
                .build();
    }

    @Test
    public void insertMentionTest() {

    }

    @Test
    public void insertMentionWithConflict() {

    }

    @Test
    public void deleteMention() {

    }

    @Test
    public void getLatestScreenName() {

    }

    @Test
    public void getLatestUsers() {


    }


    @Test
    public void trimDatabaseTrimsDatabase() {

    }

    @Test
    public void trimDatabaseDoesNotTrimSmallDatabase() {

    }

    @Test
    public void mentionsCascadeDelete() {

    }


    private String[] mockUser() {
        return null;
    }

    private String mockTweet() {
        return null;
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
