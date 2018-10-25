package daotests;

import com.klinker.android.twitter_l.data.roomdb.TalonDatabase;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import androidx.core.widget.TextViewCompat;
import androidx.room.Room;
import androidx.test.platform.app.InstrumentationRegistry;

public class FavoriteUserDaoTest extends DaoTest {

    @BeforeClass
    public static void initDatabase() {
        initTestDatabase();
    }

    @Test
    public void insertFavoriteUser() {

    }

    @Test
    public void insertFavoriteUserWithConflict() {

    }

    @Test
    public void deleteUser() {

    }

    @Test
    public void deleteAllUsers() {

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
