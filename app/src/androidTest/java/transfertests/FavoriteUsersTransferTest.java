package transfertests;

import com.klinker.android.twitter_l.data.sq_lite.FavoriteUsersSQLiteHelper;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class FavoriteUsersTransferTest extends TransferTest {

    @BeforeClass
    public static void initDatabase() {
        String tableCreation = FavoriteUsersSQLiteHelper.DATABASE_CREATE;

        initSourceDatabase(tableCreation);
        initTestDatabase();
    }


    @Test
    public void testBasicFavoriteUsersTransfer() {

    }

    @After
    public void clearDatabases() {
        clearTestDatabase();
        clearSourceDatabase(FavoriteUsersSQLiteHelper.TABLE_HOME);
    }

    @AfterClass
    public static void closeDatabase() {
        testDatabase.close();
        sourceDatabase.close();
    }
}
