package transfertests;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.klinker.android.twitter_l.data.roomdb.TalonDatabase;
import com.klinker.android.twitter_l.data.sq_lite.UserTweetsSQLiteHelper;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;

import androidx.room.Room;
import androidx.test.platform.app.InstrumentationRegistry;


public class UserTweetsTransferTest extends TransferTest {

    @BeforeClass
    public static void initDatabase() {
        initSourceDatabase();
        initTestDatabase();
    }

    @Test
    public void testBasicUserTweetsTransfer() {

    }

    @Test
    public void testTransferIfNoSourceDatabase() {

    }


    @After
    public void clearDatabases() {
        clearSourceDatabase(UserTweetsSQLiteHelper.TABLE_HOME);
        clearTestDatabase();
    }

    @AfterClass
    public static void closeDatabase() {
        testDatabase.close();
        testDatabase = null;

        sourceDatabase.close();
        sourceDatabase = null;

    }
}
