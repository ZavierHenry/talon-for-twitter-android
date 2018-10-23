package transfertests;

import com.klinker.android.twitter_l.data.sq_lite.HomeSQLiteHelper;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;


public class HomeTweetsTransferTest extends TransferTest {

    @BeforeClass
    public static void initDatabase() {
        initSourceDatabase();
        initTestDatabase();
    }



    @After
    public void clearDatabases() {
        clearSourceDatabase(HomeSQLiteHelper.TABLE_HOME);
        clearTestDatabase();
    }

    @AfterClass
    public static void closeDatabase() {
        testDatabase.close();
        sourceDatabase.close();
    }
}
