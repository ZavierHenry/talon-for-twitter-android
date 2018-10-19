package daotests;

import com.klinker.android.twitter_l.data.roomdb.TalonDatabase;

import org.junit.After;
import org.junit.Before;

public class ActivityDaoTest extends DaoTest {

    @Before
    @Override
    public void initDatabase() {

    }


    @After
    @Override
    public void closeDatabase() {
        testDatabase.close();
        testDatabase = null;
    }
}
