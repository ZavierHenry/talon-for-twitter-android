package daotests


import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import com.klinker.android.twitter_l.data.sq_lite.FavoriteUserNotificationSQLiteHelper
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.After
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test


class FavoriteUserNotificationDaoTest : DaoTest() {

    private val favoriteUserNotificationDao get() = testDatabase.favoriteUserNotificationDao()



    @Test
    fun trimDatabaseTrimsLargeDatabase() {
        val databaseSize = 5
        val trimSize = 3
        val contentValues = ContentValues()

        val ids = asTransaction {

            (0 until databaseSize).map {
                with(contentValues) {
                    put(FavoriteUserNotificationSQLiteHelper.COLUMN_TWEET_ID, it + 66)
                }
                insertIntoDatabase("favorite_user_notifications", SQLiteDatabase.CONFLICT_IGNORE, contentValues)
            }
        }

        favoriteUserNotificationDao.trimDatabase(trimSize)

        queryDatabase("SELECT id FROM favorite_user_notifications", null).use {cursor ->
            assertThat("Trim database did not trim database", cursor.count, `is`(trimSize))
        }

    }


    @Test
    fun trimDatabaseSmallDatabaseUnchanged() {
        val databaseSize = 3
        val trimSize = 5


    }


    @After
    fun clearTables() {
        clearTestDatabase()
    }


    companion object {

        @BeforeClass
        @JvmStatic fun initDatabase() {
            initTestDatabase()
        }

        @AfterClass
        @JvmStatic fun closeDatabase() {
            closeTestDatabase()
        }

    }


}