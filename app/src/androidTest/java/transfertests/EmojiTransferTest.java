package transfertests;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.klinker.android.twitter_l.data.roomdb.TalonDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import androidx.room.Room;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.test.platform.app.InstrumentationRegistry;

public class EmojiTransferTest extends TransferTest {

    @Before
    @Override
    public void initDatabase() {
        Context context = InstrumentationRegistry.getInstrumentation().getContext();

        //create emojis database
        File sourcePath = context.getDatabasePath("recent.db");
        sourceDatabase = SQLiteDatabase.openOrCreateDatabase(sourcePath, null);

        //fill source database with data

        testDatabase = Room
                .inMemoryDatabaseBuilder(context, TalonDatabase.class)
                .addCallback(TalonDatabase.transferEmojiData(context))
                .build();

        //populate database with information

    }


    @Test
    public void testDataTransferResult() {

    }


    @Test
    public void testCallbackIfNoSourceDatabase() {

    }


    @After
    @Override
    public void closeDatabase() {
        testDatabase.close();

        String path = sourceDatabase.getPath();
        sourceDatabase.close();
        SQLiteDatabase.deleteDatabase(new File(path));
    }
}
