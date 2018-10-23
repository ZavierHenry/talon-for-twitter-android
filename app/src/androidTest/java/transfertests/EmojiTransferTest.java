package transfertests;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.klinker.android.twitter_l.data.roomdb.TalonDatabase;
import com.klinker.android.twitter_l.data.roomdb.entities.Emoji;
import com.klinker.android.twitter_l.data.sq_lite.EmojiSQLiteHelper;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;

import androidx.room.Room;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.test.platform.app.InstrumentationRegistry;

public class EmojiTransferTest extends TransferTest {

    @BeforeClass
    public static void initDatabase() {
        initSourceDatabase();
        initTestDatabase();
    }


    @Test
    public void testDataTransferResult() {


    }


    @Test
    public void testCallbackIfNoSourceDatabase() {

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
