package daotests;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.klinker.android.twitter_l.data.roomdb.Hashtag;
import com.klinker.android.twitter_l.data.roomdb.TalonDatabase;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;


import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.test.platform.app.InstrumentationRegistry;


public class HashtagDaoTest {

    private TalonDatabase testDatabase = null;
    private static final int initDatabaseSize = 100;

    @Before
    public void initDatabase() {
        RoomDatabase.Callback callback = new RoomDatabase.Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
                ContentValues contentValues = new ContentValues();

                for (int i = 0; i < initDatabaseSize; i++) {
                    contentValues.put("name", "#tag" + Integer.toHexString(i));
                    db.insert("hashtags", SQLiteDatabase.CONFLICT_ABORT, contentValues);
                }
            }
        };


        testDatabase = Room
                .inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().getTargetContext(), TalonDatabase.class)
                .addCallback(callback)
                .build();
    }

    @Test
    public void trimDatabaseTrimsDatabase() {
        int databaseLimit = 60;

        testDatabase.hashtagDao().trimDatabase(databaseLimit);
        Cursor cursor = testDatabase.query("SELECT * FROM hashtags;", null);
        assertThat("Database is not being trimmed properly", cursor.getCount(), is(equalTo(databaseLimit)));
    }


    @Test
    public void noTrimDatabase_IfDatabaseTooSmall() {
        int databaseLimit = 1000;

        testDatabase.hashtagDao().trimDatabase(databaseLimit);
        Cursor cursor = testDatabase.query("SELECT * FROM hashtags;", null);
        assertThat("Trim database incorrectly trims database", cursor.getCount(), is(equalTo(initDatabaseSize)));
    }


    @Test
    public void insertHashtag() {
        Hashtag hashtag = new Hashtag("#wired25");
        testDatabase.hashtagDao().insertTag(hashtag);
        Cursor cursor = testDatabase.query("SELECT name FROM hashtags WHERE name = ?", new String[] {"#wired25"});
        Assert.assertTrue(cursor.moveToFirst());

        String name = cursor.getString(0);
        assertThat("Hashtag wasn't inserted properly", name, is(equalTo("#wired25")));

    }

    @Test
    public void getHashtagCursor() {
        Assert.fail("Unimplemented test");
    }

    @Test
    public void getHashtagList() {
        Assert.fail("Unimplemented test");
    }





    @After
    public void closeDatabase() {
        testDatabase.close();
    }

}
