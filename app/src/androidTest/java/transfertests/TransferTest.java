package transfertests;


import android.database.sqlite.SQLiteDatabase;

import com.klinker.android.twitter_l.data.roomdb.TalonDatabase;

import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

public abstract class TransferTest {

    protected TalonDatabase testDatabase = null;
    protected SQLiteDatabase sourceDatabase = null;

    protected final void applyCallback(RoomDatabase.Callback callback) {

        if (testDatabase != null) {
            callback.onCreate(testDatabase.getOpenHelper().getWritableDatabase());
        }

    }

    public abstract void initDatabase();

    public abstract void closeDatabase();

}
