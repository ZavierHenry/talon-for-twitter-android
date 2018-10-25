package transfertests;

import com.klinker.android.twitter_l.data.sq_lite.HomeSQLiteHelper;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class HomeTweetsTransferTest extends TransferTest {

    @BeforeClass
    public static void initDatabase() {
        String tableCreation = HomeSQLiteHelper.DATABASE_CREATE;
        String addConvoField = HomeSQLiteHelper.DATABASE_ADD_CONVO_FIELD;
        String addMediaLengthField = HomeSQLiteHelper.DATABASE_ADD_MEDIA_LENGTH_FIELD;

        initSourceDatabase(tableCreation, addConvoField, addMediaLengthField);
        initTestDatabase();
    }


    @Test
    public void testBasicHomeTweetsTransfer() {

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
