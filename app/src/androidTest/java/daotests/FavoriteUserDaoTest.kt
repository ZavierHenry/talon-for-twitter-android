package daotests

import com.klinker.android.twitter_l.data.roomdb.TalonDatabase

import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

import androidx.core.widget.TextViewCompat
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry

class FavoriteUserDaoTest : DaoTest() {

    private val favoriteUserDao get() = testDatabase.favoriteUserDao()

    @Test
    fun insertFavoriteUser() {

    }

    @Test
    fun insertFavoriteUserWithConflict() {

    }

    @Test
    fun deleteUser() {

    }

    @Test
    fun deleteAllUsers() {

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
