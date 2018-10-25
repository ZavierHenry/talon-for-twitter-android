package daotests;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.klinker.android.twitter_l.data.roomdb.entities.Hashtag;
import com.klinker.android.twitter_l.data.roomdb.TalonDatabase;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.test.platform.app.InstrumentationRegistry;


public class HashtagDaoTest extends DaoTest {

    @BeforeClass
    public static void initDatabase() {
        initTestDatabase();
    }

    @Test
    public void trimDatabaseTrimsDatabase() {
        int databaseLimit = 60;
        ContentValues contentValues = new ContentValues();
        int actualSize = 0;

        for (int i = 0; i < 100; i++) {
            contentValues.put("name", "#tag" + i);
            long id = testDatabase.getOpenHelper().getWritableDatabase().insert("hashtags", SQLiteDatabase.CONFLICT_IGNORE, contentValues);
            if (id != -1) {
                actualSize++;
            }
        }

        assertThat("Database setup for test failed", actualSize, greaterThan(databaseLimit));

        testDatabase.hashtagDao().trimDatabase(databaseLimit);
        Cursor cursor = testDatabase.query("SELECT * FROM hashtags;", null);
        assertThat("Database is not being trimmed properly", cursor.getCount(), is(databaseLimit));

    }

    //test that trimming is actually taking the id in consideration


    @Test
    public void noTrimDatabaseIfDatabaseTooSmall() {
        int databaseLimit = 1000;
        ContentValues contentValues = new ContentValues();
        int actualSize = 0;

        for (int i = 0; i < 100; i++) {
            contentValues.put("name", "#tag" + i);
            long id = testDatabase.getOpenHelper().getWritableDatabase().insert("hashtags", SQLiteDatabase.CONFLICT_IGNORE, contentValues);
            if (id != -1) {
                actualSize++;
            }
        }

        testDatabase.hashtagDao().trimDatabase(databaseLimit);
        Cursor cursor = testDatabase.query("SELECT * FROM hashtags;", null);
        assertThat("Trim database incorrectly trims database", cursor.getCount(), is(actualSize));
    }


    @Test
    public void insertHashtag() {
        Hashtag hashtag = new Hashtag("#wired25");
        testDatabase.hashtagDao().insertTag(hashtag);
        Cursor cursor = testDatabase.query("SELECT name FROM hashtags WHERE name = ?", new String[] {"#wired25"});
        Assert.assertTrue(cursor.moveToFirst());

        String name = cursor.getString(0);
        assertThat("Hashtag wasn't inserted properly", name, is("#wired25"));
    }

    @Test
    public void getHashtagCursor() {
    }

    @Test
    public void getHashtagList() {
    }

    @Test
    public void hashtagTableRejectsDuplicates() {
        String name = "#tag3";
        Hashtag hashtag = new Hashtag(name);
        testDatabase.hashtagDao().insertTag(hashtag);
        Cursor cursor = testDatabase.query("SELECT name FROM hashtags WHERE name = ?", new String[] {name});
        assertThat("Database incorrectly allows duplicate names", cursor.getCount(), is(1));
    }

    @After
    public void clearDatabase() {
        testDatabase.clearAllTables();
    }

    @AfterClass
    public static void closeDatabase() {
        testDatabase.close();
        testDatabase = null;
    }

}
