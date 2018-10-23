package transfertests;

import com.klinker.android.twitter_l.data.sq_lite.InteractionsSQLiteHelper;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;


public class InteractionTransferTest extends TransferTest {

    @BeforeClass
    public static void initDatabase() {
        initSourceDatabase();
        initTestDatabase();
    }



    @After
    public void clearDatabases() {
        clearSourceDatabase(InteractionsSQLiteHelper.TABLE_INTERACTIONS);
        clearTestDatabase();
    }

    @AfterClass
    public static void closeDatabase() {
        testDatabase.close();
        sourceDatabase.close();
    }
}
