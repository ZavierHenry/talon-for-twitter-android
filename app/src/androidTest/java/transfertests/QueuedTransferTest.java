package transfertests;



import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.strictmode.SqliteObjectLeakedViolation;

import com.klinker.android.twitter_l.data.roomdb.TalonDatabase;
import com.klinker.android.twitter_l.data.sq_lite.QueuedSQLiteHelper;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import java.io.File;

import androidx.room.Room;
import androidx.test.platform.app.InstrumentationRegistry;

public class QueuedTransferTest extends TransferTest {

    @BeforeClass
    public static void initDatabase() {
        initSourceDatabase();
        initTestDatabase();
    }


    @After
    public void clearDatabases() {
        clearTestDatabase();
        clearSourceDatabase(QueuedSQLiteHelper.TABLE_QUEUED);
    }

    @AfterClass
    public static void closeDatabase() {
        testDatabase.close();
        sourceDatabase.close();
    }
}
