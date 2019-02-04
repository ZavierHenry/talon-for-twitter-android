package transfertests

import android.content.ContentValues
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import com.klinker.android.twitter_l.data.roomdb.TalonDatabase
import com.klinker.android.twitter_l.data.sq_lite.FavoriteUserNotificationSQLiteHelper

import org.hamcrest.Description
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test


import transfertests.MockFavoriteUserNotificationMatcher.Companion.matchesFavoriteUserNotification


class FavoriteUserNotificationsTransferTest : TransferTest() {

    @Test
    fun transferFavoriteUserNotificationsData() {
        val notifications = List(2) { MockFavoriteUserNotification(it.toLong()) }
        val contentValues = ContentValues()

        val idCount = asTransaction {

            notifications.asSequence().map { notification ->
                notification.setContentValues(contentValues)
                insertIntoSourceDatabase("favorite_user_notifications", contentValues)
            }.count { it != -1L }
        }

        assertThat("At least two user notifications must be in source database to test properly", idCount, greaterThanOrEqualTo(2))

        applyCallback(TalonDatabase.transferFavoriteUserNotificationsData(context, sourceDatabasePath))

        queryTestDatabase("SELECT * FROM favorite_user_notifications", null).use { cursor ->
            assertThat("Incorrect number of database values", cursor.count, `is`(idCount))
            assertThat("Problem pointing to the first value of the cursor", cursor.moveToFirst())

            val matchers = notifications.map { matchesFavoriteUserNotification(it)}

            do {

                val notification = MockFavoriteUserNotification.create(cursor)
                assertThat("Notification saved is not correct", notification, anyOf(matchers))

            } while (cursor.moveToNext())
        }
    }


    @Test
    fun testTransferIfNoSourceTable() {
        applyCallback(TalonDatabase.transferFavoriteUserNotificationsData(context, sourceDatabasePath))

        queryTestDatabase("SELECT * FROM favorite_user_notifications", null).use { cursor ->
            assertThat("Somehow the database got populated", cursor.count, `is`(0))
        }

    }


    @Test
    fun testTransferIfNoSourceDatabase() {
        applyCallback(TalonDatabase.transferFavoriteUserNotificationsData(context, TransferTest.badDatabaseLocation))
    }

    @After
    fun clearDatabases() {
        clearTestDatabase()
        clearSourceDatabase(FavoriteUserNotificationSQLiteHelper.TABLE)
    }

    companion object {

        @BeforeClass
        @JvmStatic fun initDatabase() {
            val tableCreation = FavoriteUserNotificationSQLiteHelper.DATABASE_CREATE

            TransferTest.initSourceDatabase(tableCreation)
            TransferTest.initTestDatabase()
        }

        @AfterClass
        @JvmStatic fun closeDatabase() {
            TransferTest.closeSourceDatabase()
            TransferTest.closeTestDatabase()
        }
    }
}

internal class MockFavoriteUserNotification(val tweetId: Long) : MockEntity<MockFavoriteUserNotification>() {

    override fun showMismatches(other: MockFavoriteUserNotification): Collection<FieldMismatch> {
        val mismatches  = ArrayList<FieldMismatch>()

        if (tweetId != other.tweetId) {
            mismatches.add(makeMismatch("tweetId", tweetId, other.tweetId))
        }

        return mismatches
    }

    override fun setContentValues(contentValues: ContentValues) {
        with(contentValues) {
            put(FavoriteUserNotificationSQLiteHelper.COLUMN_TWEET_ID, tweetId)
        }
    }

    companion object {

        @JvmStatic fun create(cursor: Cursor) : MockFavoriteUserNotification {
            return with(cursor) {
                val tweetId = getLong(getColumnIndex("tweet_id"))
                MockFavoriteUserNotification(tweetId)
            }
        }

    }

}


internal class MockFavoriteUserNotificationMatcher private constructor(expected: MockFavoriteUserNotification) : MockMatcher<MockFavoriteUserNotification>(expected) {


    companion object {
        @JvmStatic fun matchesFavoriteUserNotification(expected: MockFavoriteUserNotification) : MockFavoriteUserNotificationMatcher {
            return MockFavoriteUserNotificationMatcher(expected)
        }
    }
}


