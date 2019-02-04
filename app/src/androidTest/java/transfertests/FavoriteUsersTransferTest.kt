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


    @Test
    fun testBasicFavoriteUsersTransfer() {

    }


    @Test
    fun tastTransferIfEmptySourceTable() {

    }

    @Test
    fun testTransferIfNoSourceDatabase() {
        applyCallback(TalonDatabase.transferFavoriteUsersData(context, TransferTest.badDatabaseLocation))
    }

    @After
    fun clearDatabases() {
        clearTestDatabase()
        clearSourceDatabase(FavoriteUsersSQLiteHelper.TABLE_HOME)
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