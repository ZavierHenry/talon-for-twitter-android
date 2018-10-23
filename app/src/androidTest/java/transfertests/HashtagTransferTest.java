package transfertests;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.klinker.android.twitter_l.data.sq_lite.HashtagSQLiteHelper;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import java.io.File;

import androidx.test.platform.app.InstrumentationRegistry;

public class HashtagTransferTest extends TransferTest {

    @BeforeClass
    public static void initDatabase() {
        initSourceDatabase();
        initTestDatabase();
    }



    @After
    public void clearDatabases() {
        clearTestDatabase();
        clearSourceDatabase(HashtagSQLiteHelper.TABLE_HASHTAGS);
    }

    @AfterClass
    public static void closeDatabase() {
        testDatabase.close();
        sourceDatabase.close();
    }
}
