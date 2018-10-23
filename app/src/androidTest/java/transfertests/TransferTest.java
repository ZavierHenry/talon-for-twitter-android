package transfertests;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.klinker.android.twitter_l.data.roomdb.TalonDatabase;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.test.platform.app.InstrumentationRegistry;

public abstract class TransferTest {

    protected static TalonDatabase testDatabase = null;
    protected static SQLiteDatabase sourceDatabase = null;

    protected static void initTestDatabase() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        testDatabase = Room.inMemoryDatabaseBuilder(context, TalonDatabase.class).build();
    }

    protected static void initSourceDatabase() {
        SQLiteDatabase.OpenParams params = new SQLiteDatabase.OpenParams.Builder()
                .addOpenFlags(SQLiteDatabase.CREATE_IF_NECESSARY)
                .addOpenFlags(SQLiteDatabase.OPEN_READWRITE)
                .build();

        sourceDatabase = SQLiteDatabase.createInMemory(params);
    }

    protected static void clearTestDatabase() {
        testDatabase.clearAllTables();
    }

    protected static void clearSourceDatabase(String tableName) {
        sourceDatabase.delete(tableName, null, null);
    }

}
