package daotests;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import androidx.room.Dao;

public class DirectMessageDaoTest extends DaoTest {


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
