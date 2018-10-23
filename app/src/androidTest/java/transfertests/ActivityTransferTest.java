package transfertests;

import android.content.Context;

import com.klinker.android.twitter_l.data.roomdb.TalonDatabase;
import com.klinker.android.twitter_l.data.sq_lite.ActivitySQLiteHelper;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import java.util.concurrent.atomic.AtomicLong;


public class ActivityTransferTest extends TransferTest {

    @BeforeClass
    public static void initDatabase() {
        initSourceDatabase();
        initTestDatabase();
    }

    @After
    public void clearDatabases() {
        clearTestDatabase();
        clearSourceDatabase(ActivitySQLiteHelper.TABLE_ACTIVITY);
    }

    @AfterClass
    public void closeDatabase() {
        testDatabase.close();
        sourceDatabase.close();
    }
}
