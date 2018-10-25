package transfertests;

import android.content.Context;
import android.database.Cursor;

import com.klinker.android.twitter_l.data.roomdb.TalonDatabase;
import com.klinker.android.twitter_l.data.sq_lite.ActivitySQLiteHelper;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicLong;


public class ActivityTransferTest extends TransferTest {

    @BeforeClass
    public static void initDatabase() {
        String tableCreation = ActivitySQLiteHelper.DATABASE_CREATE;
        String addConvoField = ActivitySQLiteHelper.DATABASE_ADD_CONVO_FIELD;
        String addMediaLengthField = ActivitySQLiteHelper.DATABASE_ADD_MEDIA_LENGTH_FIELD;
        initSourceDatabase(tableCreation, addConvoField, addMediaLengthField);
        initTestDatabase();
    }


    @Test
    public void testBasicActivityTransfer() {

        //fill activity database
        //call onCreate of activity callback
        //test results of transfer
    }


    @Test
    public void testTransferIfEmptySourceTable() {

    }

    @Test
    public void testTransferIfNoSourceDatabase() {
        AtomicLong userId = new AtomicLong(-2);
        applyCallback(TalonDatabase.transferActivityData(null, userId));
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
