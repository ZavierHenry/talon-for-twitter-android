package transfertests;

import android.database.Cursor;

import com.klinker.android.twitter_l.data.sq_lite.DMSQLiteHelper;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DirectMessagesTransferTest extends TransferTest {

    @BeforeClass
    public static void initDatabase() {
        String tableCreation = DMSQLiteHelper.DATABASE_CREATE;
        String addMediaLengthField = DMSQLiteHelper.DATABASE_ADD_MEDIA_LENGTH_FIELD;

        initSourceDatabase(tableCreation, addMediaLengthField);
        initTestDatabase();
    }


    @Test
    public void testDirectMessagesTransfer() {

        //populate source database
        //call callback onCreate
        //test direct message transfer results

        Cursor testCursor = testDatabase.query("SELECT * FROM direct_messages", null);

    }

    @Test
    public void testTransferIfEmptyTable() {

    }

    @Test
    public void testTransferIfNoSourceDatabase() {

    }


    @After
    public void clearDatabases() {
        clearTestDatabase();
        clearSourceDatabase(DMSQLiteHelper.TABLE_DM);
    }

    @AfterClass
    public static void closeDatabase() {
        testDatabase.close();
        sourceDatabase.close();
    }
}
