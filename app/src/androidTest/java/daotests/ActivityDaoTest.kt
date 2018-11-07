package daotests

import com.klinker.android.twitter_l.data.roomdb.TalonDatabase

import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass

class ActivityDaoTest : DaoTest() {


    @After
    fun clearTables() {
        DaoTest.testDatabase?.clearAllTables()
    }

    companion object {

        @BeforeClass
        fun initDatabase() {

        }

        @AfterClass
        fun closeDatabase() {
            DaoTest.testDatabase?.close()
            DaoTest.testDatabase = null
        }
    }
}
