package transfertests;

import com.klinker.android.twitter_l.data.roomdb.entities.FavoriteTweet;
import com.klinker.android.twitter_l.data.sq_lite.FavoriteTweetsSQLiteHelper;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class FavoriteTweetsTransferTest extends TransferTest {

    @BeforeClass
    public static void initDatabase() {
        String tableCreation = FavoriteTweetsSQLiteHelper.TABLE_FAVORITE_TWEETS;
        String addConvField = FavoriteTweetsSQLiteHelper.DATABASE_ADD_CONVO_FIELD;
        String addMediaLengthField = FavoriteTweetsSQLiteHelper.DATABASE_ADD_MEDIA_LENGTH_FIELD;

        initSourceDatabase(tableCreation, addConvField, addMediaLengthField);
        initTestDatabase();
    }


    @Test
    public void testBasicFavoriteTweetsTransfer() {

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
        clearSourceDatabase(FavoriteTweetsSQLiteHelper.TABLE_FAVORITE_TWEETS);
    }

    @AfterClass
    public static void closeDatabase() {
        testDatabase.close();
        sourceDatabase.close();
    }
}
