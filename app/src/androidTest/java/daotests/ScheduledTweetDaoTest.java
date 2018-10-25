package daotests;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.strictmode.SqliteObjectLeakedViolation;

import com.klinker.android.twitter_l.data.roomdb.TalonDatabase;
import com.klinker.android.twitter_l.data.roomdb.entities.ScheduledTweet;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import androidx.room.Room;
import androidx.test.platform.app.InstrumentationRegistry;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

public class ScheduledTweetDaoTest extends DaoTest {

    @BeforeClass
    public void initDatabase() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        testDatabase = Room.inMemoryDatabaseBuilder(context, TalonDatabase.class)
                .build();
    }

    @Test
    public void insertScheduledTweet() {
        ScheduledTweet scheduledTweet = new ScheduledTweet(1, "This is a test message", 100000, 1);
        testDatabase.scheduledTweetDao().insertScheduledTweet(scheduledTweet);

        Cursor cursor = testDatabase.query("SELECT alarm_id FROM scheduled_tweets WHERE alarm_id = ?", new String[] { Long.toString(scheduledTweet.alarmId)});
        assertThat("Scheduled tweet is not inserted in the database", cursor.getCount(), is(1));
    }

    @Test
    public void deleteScheduledTweet() {
        ScheduledTweet scheduledTweet = new ScheduledTweet(76, "This is a test message", 130000, 1);
        ContentValues contentValues = new ContentValues();
        contentValues.put("alarm_id", scheduledTweet.alarmId);
        contentValues.put("text", scheduledTweet.text);
        contentValues.put("account", scheduledTweet.account);
        contentValues.put("time", scheduledTweet.time);

        long id = testDatabase.getOpenHelper().getWritableDatabase().insert("scheduled_tweets", SQLiteDatabase.CONFLICT_IGNORE, contentValues);
        assertThat("Initial setup for deleteScheduledTweet test failed", id, not(-1));


        testDatabase.scheduledTweetDao().deleteScheduledTweet(scheduledTweet);
        Cursor cursor = testDatabase.query("SELECT alarm_id FROM scheduled_tweets WHERE alarm_id = ?", new String[] {Long.toString(scheduledTweet.alarmId)});
        assertThat("Scheduled tweet is not deleted from the database", cursor.getCount(), is(0));
    }


    @Test
    public void getScheduledTweets() {

        int account = 1;
        ContentValues contentValues = new ContentValues();
        contentValues.put("time", 3000);
        contentValues.put("text", "This is a scheduled tweet test string");

        for (int i = 0; i < 400; i++) {
            contentValues.put("account", i < 300 ? 1 : 2);
            contentValues.put("alarm_id", i);
            testDatabase.getOpenHelper().getWritableDatabase().insert("scheduled_tweets", SQLiteDatabase.CONFLICT_IGNORE, contentValues);
        }

        //assert some setup assertions

        List<ScheduledTweet> scheduledTweets = testDatabase.scheduledTweetDao().getScheduledTweets(1);
        assertThat("Querying for a list of scheduled tweets did not return tweets", scheduledTweets, notNullValue());

    }


    @Test
    public void getEarliestScheduledTweet() {

        long time = 10000;

        ScheduledTweet scheduledTweet = testDatabase.scheduledTweetDao().getEarliestScheduledTweets(time);
    }


    @Test
    public void getNoScheduledTweetIfTimeisTooEarly() {

        long time = 10000;


        ScheduledTweet scheduledTweet = testDatabase.scheduledTweetDao().getEarliestScheduledTweets(time);
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
