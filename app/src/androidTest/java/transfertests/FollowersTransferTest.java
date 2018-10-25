package transfertests;

import com.klinker.android.twitter_l.data.sq_lite.FollowersSQLiteHelper;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class FollowersTransferTest extends TransferTest {

    @BeforeClass
    public static void initDatabase() {
        String tableCreation = FollowersSQLiteHelper.DATABASE_CREATE;

        initSourceDatabase(tableCreation);
        initTestDatabase();
    }


    @Test
    public void testBasicFollowersTransfer() {

    }


    @After
    public void clearDatabases() {
        clearSourceDatabase(FollowersSQLiteHelper.TABLE_HOME);
        clearTestDatabase();
    }


    @AfterClass
    public static void closeDatabase() {
        testDatabase.close();
        testDatabase = null;

        sourceDatabase.close();
        sourceDatabase = null;
    }
}
