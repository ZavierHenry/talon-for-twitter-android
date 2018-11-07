package transfertests

import com.klinker.android.twitter_l.data.roomdb.TalonDatabase
import com.klinker.android.twitter_l.data.sq_lite.FavoriteUserNotificationSQLiteHelper

import org.hamcrest.Description
import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

class FavoriteUserNotificationsTransferTest : TransferTest() {


    @Test
    fun transferFavoriteUserNotificationsData() {

    }

    @Test
    fun testTransferIfNoSourceTable() {

    }


    @Test
    fun testTransferIfNoSourceDatabase() {
        applyCallback(TalonDatabase.transferFavoriteUserNotificationsData(TransferTest.badDatabaseLocation))
    }

    @After
    fun clearDatabases() {
        TransferTest.clearTestDatabase()
        TransferTest.clearSourceDatabase(FavoriteUserNotificationSQLiteHelper.TABLE)
    }

    companion object {

        @BeforeClass
        fun initDatabase() {
            val tableCreation = FavoriteUserNotificationSQLiteHelper.DATABASE_CREATE

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

internal class MockFavoriteUserNotification

internal class MockFavoriteUserNotificationMatcher private constructor(expected: MockFavoriteUserNotification) : MockMatcher<MockFavoriteUserNotification>(expected) {

    override fun matchesSafely(item: MockFavoriteUserNotification): Boolean {
        return false
    }

    override fun describeMismatchSafely(item: MockFavoriteUserNotification, mismatchDescription: Description) {

    }

    companion object {

        fun matchesFavoriteUserNotification(expected: MockFavoriteUserNotification): MockFavoriteUserNotificationMatcher {
            return MockFavoriteUserNotificationMatcher(expected)
        }
    }
}

