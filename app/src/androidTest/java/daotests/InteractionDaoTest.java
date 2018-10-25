package daotests;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.klinker.android.twitter_l.data.roomdb.TalonDatabase;
import com.klinker.android.twitter_l.data.roomdb.daos.InteractionDao;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import androidx.room.Room;
import androidx.test.platform.app.InstrumentationRegistry;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

public class InteractionDaoTest extends DaoTest {

    @Before
    public static void initDatabase() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        testDatabase = Room.inMemoryDatabaseBuilder(context, TalonDatabase.class).build();
    }

    @Test
    public void insertFollowerIntraction() {

    }

    @Test
    public void insertRetweetInteraction() {

    }

    @Test
    public void insertFavoriteInteraction() {

    }

    @Test
    public void insertFavoriteUserInteraction() {

    }

    @Test
    public void insertQuotedTweetInteraction() {

    }

    @Test
    public void deleteAllInteractions() {

    }

    @Test
    public void getUnreadCount() {

    }

    @Test
    public void markRead() {

    }

    @Test
    public void getUsers() {

    }

    @Test
    public void markAllRead() {

    }

    @Test
    public void trimDatabaseTrimsDatabase() {
        int trimSize = 60;
        int account = 1;
        Cursor cursor;

        //load data into database

        for (int i = 0; i < 400; i++) {

        }


        cursor = testDatabase.query("SELECT id FROM interactions WHERE account = ?", new String[] {Integer.toString(account)});
        assertThat("Setup for loading data into database failed", cursor.getCount(), greaterThan(trimSize));

        testDatabase.interactionDao().trimDatabase(account, trimSize);
        cursor = testDatabase.query("SELECT id FROM interactions WHERE account = ?", new String[] {Integer.toString(account)});
        assertThat("Database did not trim to specified size", cursor.getCount(), is(trimSize));


    }

    @Test
    public void trimDatabaseWithSmallDatabase() {

        int trimSize = 10000;
        int account = 1;

        //load data into database

        testDatabase.interactionDao().trimDatabase(account, trimSize);


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
