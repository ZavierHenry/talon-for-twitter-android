package daotests;


import android.content.Context;

import com.klinker.android.twitter_l.data.roomdb.TalonDatabase;

import androidx.room.Room;
import androidx.test.platform.app.InstrumentationRegistry;

public abstract class DaoTest {
    protected static TalonDatabase testDatabase = null;

    protected static void initTestDatabase() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        testDatabase = Room.inMemoryDatabaseBuilder(context, TalonDatabase.class).build();
    }

}
