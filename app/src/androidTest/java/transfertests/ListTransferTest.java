package transfertests;

import com.klinker.android.twitter_l.data.sq_lite.ListSQLiteHelper;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

public class ListTransferTest extends TransferTest {

    @BeforeClass
    public static void initDatabase() {
        String tableCreation = ListSQLiteHelper.DATABASE_CREATE;
        String addMediaLengthField = ListSQLiteHelper.DATABASE_ADD_MEDIA_LENGTH_FIELD;

        initSourceDatabase(tableCreation, addMediaLengthField);
        initTestDatabase();
    }


    @Test
    public void testBasicListTransfer() {

    }

    @Test
    public void testTransferIfNoSourceDatabase() {

    }


    @After
    public void clearDatabases() {
        clearTestDatabase();
        clearSourceDatabase(ListSQLiteHelper.TABLE_HOME);
    }

    @AfterClass
    public static void closeDatabase() {
        testDatabase.close();
        sourceDatabase.close();
    }
}
