package transfertests;

import com.klinker.android.twitter_l.data.sq_lite.InteractionsSQLiteHelper;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class InteractionTransferTest extends TransferTest {

    @BeforeClass
    public static void initDatabase() {
        String tableCreation = InteractionsSQLiteHelper.DATABASE_CREATE;

        initSourceDatabase(tableCreation);
        initTestDatabase();
    }

    @Test
    public void testBasicInteractionsTransfer() {

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
