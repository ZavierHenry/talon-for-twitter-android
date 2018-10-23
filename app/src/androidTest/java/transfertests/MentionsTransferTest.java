package transfertests;

import com.klinker.android.twitter_l.data.sq_lite.MentionsSQLiteHelper;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;


public class MentionsTransferTest extends TransferTest {

    @BeforeClass
    public static void initDatabase() {
        initSourceDatabase();
        initTestDatabase();
    }

    @After
    public void clearDatabases() {
        clearTestDatabase();
        clearSourceDatabase(MentionsSQLiteHelper.TABLE_MENTIONS);
    }

    @AfterClass
    public static void closeDatabase() {
        testDatabase.close();
        sourceDatabase.close();
    }
}
