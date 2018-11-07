package daotests

import android.content.Context

import com.klinker.android.twitter_l.data.roomdb.TalonDatabase

import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass

import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry

class UserDaoTest : DaoTest() {


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
