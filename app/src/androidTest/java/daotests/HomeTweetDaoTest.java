package daotests;

import org.junit.After;
import org.junit.Before;

public class HomeTweetDaoTest extends DaoTest {

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
