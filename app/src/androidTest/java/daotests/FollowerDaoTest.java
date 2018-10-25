package daotests;

import com.klinker.android.twitter_l.data.roomdb.TalonDatabase;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

public class FollowerDaoTest extends DaoTest {


    @BeforeClass
    public static void initDatabase() {

    }


    @After
    public void clearTables() {
        testDatabase.clearAllTables();
    }

    @AfterClass
    public static void closeDatabase() {
        testDatabase.close();
        testDatabase = null;
    }
}
