package daotests

import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass

import androidx.room.Dao

class DirectMessageDaoTest : DaoTest() {


    @After
    fun clearTables() {
        DaoTest.clearTestDatabase()
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
