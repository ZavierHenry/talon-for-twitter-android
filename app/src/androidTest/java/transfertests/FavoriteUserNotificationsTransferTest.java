package transfertests;

import com.klinker.android.twitter_l.data.sq_lite.FavoriteUserNotificationSQLiteHelper;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class FavoriteUserNotificationsTransferTest extends TransferTest {

    @BeforeClass
    public static void initDatabase() {
        String tableCreation = FavoriteUserNotificationSQLiteHelper.DATABASE_CREATE;

        initSourceDatabase(tableCreation);
        initTestDatabase();
    }


    @Test
    public void transferFavoriteUserNotificationsData() {

    }

    @After
    public void clearDatabases() {
        clearTestDatabase();
        clearSourceDatabase(FavoriteUserNotificationSQLiteHelper.TABLE);
    }

    @AfterClass
    public static void closeDatabase() {
        testDatabase.close();
        sourceDatabase.close();
    }
}
