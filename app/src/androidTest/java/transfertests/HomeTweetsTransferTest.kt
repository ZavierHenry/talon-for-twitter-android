package transfertests

import android.content.ContentValues
import android.database.Cursor

import com.klinker.android.twitter_l.data.sq_lite.HomeSQLiteHelper

import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher
import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test


class HomeTweetsTransferTest : TransferTest() {


    @Test
    fun testBasicHomeTweetsTransfer() {

    }


    @After
    fun clearDatabases() {
        clearSourceDatabase(HomeSQLiteHelper.TABLE_HOME)
        clearTestDatabase()
    }

    companion object {

        @BeforeClass
        fun initDatabase() {
            val tableCreation = HomeSQLiteHelper.DATABASE_CREATE
            val addConvoField = HomeSQLiteHelper.DATABASE_ADD_CONVO_FIELD
            val addMediaLengthField = HomeSQLiteHelper.DATABASE_ADD_MEDIA_LENGTH_FIELD

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
