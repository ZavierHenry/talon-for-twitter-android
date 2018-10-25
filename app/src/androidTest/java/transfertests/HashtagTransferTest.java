package transfertests;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.strictmode.SqliteObjectLeakedViolation;

import com.klinker.android.twitter_l.data.roomdb.TalonDatabase;
import com.klinker.android.twitter_l.data.roomdb.entities.Hashtag;
import com.klinker.android.twitter_l.data.sq_lite.HashtagSQLiteHelper;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;

import androidx.test.platform.app.InstrumentationRegistry;
import com.klinker.android.twitter_l.data.sq_lite.HashtagSQLiteHelper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class HashtagTransferTest extends TransferTest {


    @BeforeClass
    public static void initDatabase() {
        String tableCreation = HashtagSQLiteHelper.DATABASE_CREATE;

        initSourceDatabase(tableCreation);
        initTestDatabase();
    }


    @Test
    public void testBasicHashtagTransfer() {
        //fill table with data
        applyCallback(TalonDatabase.transferHashtagData(sourceDatabase.getPath()));

        //check table

    }

    @Test
    public void testTransferIfEmptyTable() {

        //copy source database to prevent deletion error

        applyCallback(TalonDatabase.transferHashtagData(sourceDatabase.getPath()));
        Cursor cursor = testDatabase.query("SELECT * FROM hashtags", null);
        assertThat("Something went wrong in the database transfer", cursor, notNullValue());
        assertThat("Something went wrong in the database transfer", cursor.getCount(), is(0));
        cursor.close();

    }


    @Test
    public void testTransferIfNoSource() {
        TalonDatabase.transferHashtagData(null).onCreate(testDatabase.getOpenHelper().getWritableDatabase());
    }


    @After
    public void clearDatabases() {
        clearTestDatabase();
        clearSourceDatabase(HashtagSQLiteHelper.TABLE_HASHTAGS);
    }

    @AfterClass
    public static void closeDatabase() {
        testDatabase.close();
        testDatabase = null;

        sourceDatabase.close();
        sourceDatabase = null;
    }
}
