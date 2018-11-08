package transfertests

import com.klinker.android.twitter_l.data.sq_lite.InteractionsSQLiteHelper

import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test


class InteractionTransferTest : TransferTest() {

    @Test
    fun testBasicInteractionsTransfer() {

    }


    @After
    fun clearDatabases() {
        clearSourceDatabase(InteractionsSQLiteHelper.TABLE_INTERACTIONS)
        clearTestDatabase()
    }

    companion object {

        @BeforeClass
        fun initDatabase() {
            val tableCreation = InteractionsSQLiteHelper.DATABASE_CREATE

            TransferTest.initSourceDatabase(tableCreation)
            TransferTest.initTestDatabase()
        }

        @AfterClass
        fun closeDatabase() {
            TransferTest.closeSourceDatabase()
            TransferTest.closeTestDatabase()
        }
    }
}
