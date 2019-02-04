package daotests

import org.junit.After
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test

class FavoriteUserDaoTest : DaoTest() {

    private val favoriteUserDao get() = testDatabase.favoriteUserDao()

    @Test
    fun insertFavoriteUser() {

    }


    @Test
    fun insertFavoriteUserTwitterIdSaved() {

    }


    @Test
    fun insertFavoriteUserScreenNameSaved() {

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
