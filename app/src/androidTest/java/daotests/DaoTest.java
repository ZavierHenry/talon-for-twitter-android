package daotests;

import com.klinker.android.twitter_l.data.roomdb.TalonDatabase;

public abstract class DaoTest {

    protected TalonDatabase testDatabase = null;

    public abstract void initDatabase();

    public abstract void closeDatabase();

}
