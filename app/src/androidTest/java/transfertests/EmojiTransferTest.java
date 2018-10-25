package transfertests;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.klinker.android.twitter_l.data.roomdb.TalonDatabase;
import com.klinker.android.twitter_l.data.roomdb.entities.Emoji;
import com.klinker.android.twitter_l.data.sq_lite.EmojiSQLiteHelper;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;

import androidx.room.Room;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.test.platform.app.InstrumentationRegistry;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

public class EmojiTransferTest extends TransferTest {

    @BeforeClass
    public static void initDatabase() {
        String tableCreation = EmojiSQLiteHelper.DATABASE_CREATE;
        initSourceDatabase(tableCreation);
        initTestDatabase();
    }


    @Test
    public void testDataTransferResult() {
        Cursor sourceCursor, testCursor;

        applyCallback(TalonDatabase.transferEmojiData(sourceDatabase.getPath()));

        testCursor = testDatabase.query("SELECT * FROM emojis", null);
        assertThat(testCursor, notNullValue());


    }


    @Test
    public void testTransferIfEmptyTable() {

    }


    @Test
    public void testCallbackIfNoSourceDatabase() {
        applyCallback(TalonDatabase.transferEmojiData(null));
    }


    @After
    public void clearDatabases() {
        clearTestDatabase();
        clearSourceDatabase(EmojiSQLiteHelper.TABLE_RECENTS);
    }


    @AfterClass
    public static void closeDatabase() {
        testDatabase.close();
        sourceDatabase.close();
    }


}
