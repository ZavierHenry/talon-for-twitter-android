package transfertests

import android.content.ContentValues
import android.database.Cursor

import com.klinker.android.twitter_l.data.roomdb.entities.FavoriteTweet
import com.klinker.android.twitter_l.data.sq_lite.FavoriteTweetsSQLiteHelper

import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher
import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test


class FavoriteTweetsTransferTest : TransferTest() {


    @Test
    fun testBasicFavoriteTweetsTransfer() {

    }

    @Test
    fun testTransferIfEmptyTable() {

    }

    @Test
    fun testTransferIfNoSourceDatabase() {

    }

    @After
    fun clearDatabases() {
        clearTestDatabase()
        clearSourceDatabase(FavoriteTweetsSQLiteHelper.TABLE_FAVORITE_TWEETS)
    }

    companion object {

        @BeforeClass
        fun initDatabase() {
            val tableCreation = FavoriteTweetsSQLiteHelper.TABLE_FAVORITE_TWEETS
            val addConvField = FavoriteTweetsSQLiteHelper.DATABASE_ADD_CONVO_FIELD
            val addMediaLengthField = FavoriteTweetsSQLiteHelper.DATABASE_ADD_MEDIA_LENGTH_FIELD

            TransferTest.initSourceDatabase(tableCreation, addConvField, addMediaLengthField)
            TransferTest.initTestDatabase()
        }

        @AfterClass
        fun closeDatabase() {
            TransferTest.closeTestDatabase()
            TransferTest.closeSourceDatabase()
        }
    }
}