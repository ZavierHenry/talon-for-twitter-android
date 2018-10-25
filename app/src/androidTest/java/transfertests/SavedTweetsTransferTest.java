package transfertests;

import android.app.Instrumentation;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.klinker.android.twitter_l.data.roomdb.TalonDatabase;
import com.klinker.android.twitter_l.data.sq_lite.SavedTweetSQLiteHelper;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.test.platform.app.InstrumentationRegistry;

public class SavedTweetsTransferTest extends TransferTest {

    @BeforeClass
    public static void initDatabase() {
        initSourceDatabase();
        initTestDatabase();
    }

    @Test
    public void testBasicSavedTweetsTransfer() {

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
        clearSourceDatabase(SavedTweetSQLiteHelper.TABLE_HOME);
    }


    @AfterClass
    public static void closeDatabase() {
        testDatabase.close();
        sourceDatabase.close();
    }
}
