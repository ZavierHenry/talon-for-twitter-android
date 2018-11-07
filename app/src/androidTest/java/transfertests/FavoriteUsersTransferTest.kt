package transfertests

import android.content.ContentValues
import android.database.Cursor

import com.klinker.android.twitter_l.data.roomdb.TalonDatabase
import com.klinker.android.twitter_l.data.roomdb.entities.FavoriteUser
import com.klinker.android.twitter_l.data.roomdb.entities.User
import com.klinker.android.twitter_l.data.sq_lite.FavoriteUsersSQLiteHelper

import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher
import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

import java.util.HashMap
import java.util.concurrent.atomic.AtomicLong

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.notNullValue
import transfertests.MockUserMatcher.Companion.matchesUser

class FavoriteUsersTransferTest : TransferTest() {

    private val userLabeler: AtomicLong = AtomicLong(-2L)
    private val users: HashMap<String, User> = HashMap()

    @Before
    fun resetUserLabeler() {
        userLabeler.set(-2L)
    }


    @Test
    fun testBasicFavoriteUsersTransfer() {
        val contentValues = ContentValues()
        val insertedFavoriteUsers = HashMap<Long, MockFavoriteUser>()


        TransferTest.sourceDatabase!!.beginTransaction()

        for (i in 0..9) {
            val user = MockUser(i.toLong(), "User $i", "screenname_$i", "https://img.twitter.com/img/$i.jpg", false)
            val favoriteUser = MockFavoriteUser(1 % 2 + 1, user)
            favoriteUser.setContentValues(contentValues)
            val insertId = TransferTest.sourceDatabase!!.insert(FavoriteUsersSQLiteHelper.TABLE_HOME, null, contentValues)
            if (insertId != -1L) {
                insertedFavoriteUsers[insertId] = favoriteUser
            }
        }

        TransferTest.sourceDatabase!!.setTransactionSuccessful()
        TransferTest.sourceDatabase!!.endTransaction()

        assertThat("At least one element must be inserted into the source database to run this test properly", !insertedFavoriteUsers.isEmpty())
        applyCallback(TalonDatabase.transferFavoriteUsersData(TransferTest.sourceDatabase!!.path, users)) //add userLabeler

        val cursor = TransferTest.testDatabase!!.query("SELECT * FROM favorite_users JOIN users ON user_id = users.id", null)
        assertThat("Incorrect nunmber of entries in the database", cursor.count, `is`(insertedFavoriteUsers.size))

        cursor.moveToFirst()

        do {


        } while (cursor.moveToNext())


    }


    @Test
    fun tastTransferIfEmptySourceTable() {

    }

    @Test
    fun testTransferIfNoSourceDatabase() {
        applyCallback(TalonDatabase.transferFavoriteUsersData(TransferTest.badDatabaseLocation, users))
    }

    @After
    fun clearDatabases() {
        TransferTest.clearTestDatabase()
        TransferTest.clearSourceDatabase(FavoriteUsersSQLiteHelper.TABLE_HOME)
    }

    companion object {

        @BeforeClass
        fun initDatabase() {
            val tableCreation = FavoriteUsersSQLiteHelper.DATABASE_CREATE

            TransferTest.initSourceDatabase(tableCreation)
            TransferTest.initTestDatabase()
        }

        @AfterClass
        fun closeDatabase() {
            TransferTest.closeTestDatabase()
            TransferTest.closeSourceDatabase()
        }
    }
}

internal class MockFavoriteUser(val account: Int, val referencedUser: MockUser) {


    fun setContentValues(contentValues: ContentValues) {
        contentValues.put(FavoriteUsersSQLiteHelper.COLUMN_ACCOUNT, account)
        contentValues.put(FavoriteUsersSQLiteHelper.COLUMN_NAME, referencedUser.name)
        contentValues.put(FavoriteUsersSQLiteHelper.COLUMN_SCREEN_NAME, referencedUser.screenName)
        contentValues.put(FavoriteUsersSQLiteHelper.COLUMN_PRO_PIC, referencedUser.profilePic)
        contentValues.put(FavoriteUsersSQLiteHelper.COLUMN_ID, referencedUser.id)
    }

}


internal class MockFavoriteUserMatcher private constructor(private val expected: MockFavoriteUser) : TypeSafeMatcher<MockFavoriteUser>() {

    override fun matchesSafely(item: MockFavoriteUser): Boolean {
        return expected.account == item.account && matchesUser(expected.referencedUser).matchesSafely(item.referencedUser)
    }

    override fun describeTo(description: Description) {
        description.appendText("All fields should be equal")
    }

    override fun describeMismatchSafely(item: MockFavoriteUser, description: Description) {

    }

    companion object {

        fun matchesFavoriteUser(expected: MockFavoriteUser): MockFavoriteUserMatcher {
            return MockFavoriteUserMatcher(expected)
        }
    }

}