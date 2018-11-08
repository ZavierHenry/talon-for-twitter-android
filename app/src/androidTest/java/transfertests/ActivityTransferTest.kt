package transfertests

import android.content.Context
import android.database.Cursor

import com.klinker.android.twitter_l.data.roomdb.TalonDatabase
import com.klinker.android.twitter_l.data.sq_lite.ActivitySQLiteHelper

import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

import java.util.concurrent.atomic.AtomicLong


class ActivityTransferTest : TransferTest() {


    @Test
    fun testBasicActivityTransfer() {

        //fill activity database
        //call onCreate of activity callback
        //test results of transfer
    }


    @Test
    fun testTransferIfEmptySourceTable() {

    }

    @Test
    fun testTransferIfNoSourceDatabase() {
        val userId = AtomicLong(-2L)
        applyCallback(TalonDatabase.transferActivityData(TransferTest.badDatabaseLocation, userId))
    }

    @After
    fun clearDatabases() {
        clearTestDatabase()
        clearSourceDatabase(ActivitySQLiteHelper.TABLE_ACTIVITY)
    }


    companion object {

        @BeforeClass
        fun initDatabase() {
            val tableCreation = ActivitySQLiteHelper.DATABASE_CREATE
            val addConvoField = ActivitySQLiteHelper.DATABASE_ADD_CONVO_FIELD
            val addMediaLengthField = ActivitySQLiteHelper.DATABASE_ADD_MEDIA_LENGTH_FIELD

            TransferTest.initSourceDatabase(tableCreation, addConvoField, addMediaLengthField)
            TransferTest.initTestDatabase()
        }

        @AfterClass
        fun closeDatabase() {
            TransferTest.closeSourceDatabase()
            TransferTest.closeTestDatabase()
        }
    }
}

internal class MockActivity
