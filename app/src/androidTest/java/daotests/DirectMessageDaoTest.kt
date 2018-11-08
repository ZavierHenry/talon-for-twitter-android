package daotests

import androidx.room.Dao
import com.klinker.android.twitter_l.data.roomdb.daos.DirectMessageDao
import org.junit.*

class DirectMessageDaoTest : DaoTest() {

    private val directMessageDao get() = testDatabase.directMessageDao()


    @Test
    fun insertDirectMessage() {

    }



    @Test
    fun trimDatabaseTrimsDatabase() {

    }


    @Test
    fun trimDatabaseDoesntTrimSmallDatabase() {

    }



    @After
    fun clearTables() {
        clearTestDatabase()
    }



    companion object {


        @BeforeClass
        fun initDatabase() {
            DaoTest.initTestDatabase()
        }

        @AfterClass
        fun closeDatabase() {
            DaoTest.closeTestDatabase()
        }
    }
}
