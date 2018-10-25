package transfertests;

import com.klinker.android.twitter_l.data.sq_lite.MentionsSQLiteHelper;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class MentionsTransferTest extends TransferTest {

    @BeforeClass
    public static void initDatabase() {
        initSourceDatabase();
        initTestDatabase();
    }

    @Test
    public void testBasicMentionsTransfer() {

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
        clearSourceDatabase(MentionsSQLiteHelper.TABLE_MENTIONS);
    }

    @AfterClass
    public static void closeDatabase() {
        testDatabase.close();
        sourceDatabase.close();
    }
}
