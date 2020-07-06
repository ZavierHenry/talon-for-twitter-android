package com.klinker.android.twitter_l.transfertests

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.klinker.android.twitter_l.data.roomdb.transfers.FavoriteUserTransfer
import com.klinker.android.twitter_l.data.sq_lite.FavoriteUsersSQLiteHelper
import com.klinker.android.twitter_l.mockentities.MockFavoriteUser
import com.klinker.android.twitter_l.mockentities.matchers.MockEntityMatcher.Companion.matchesMockEntity
import com.klinker.android.twitter_l.mockentities.transferentities.MockTransferFavoriteUser
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class FavoriteUserTransferDatabaseTest {

    private val sourceColumns = listOf(
            "${FavoriteUsersSQLiteHelper.COLUMN_ID} integer primary key",
            "${FavoriteUsersSQLiteHelper.COLUMN_ACCOUNT} integer",
            "${FavoriteUsersSQLiteHelper.COLUMN_NAME} text",
            "${FavoriteUsersSQLiteHelper.COLUMN_PRO_PIC} text",
            "${FavoriteUsersSQLiteHelper.COLUMN_SCREEN_NAME} text"
    )

    @get:Rule val database = with(ApplicationProvider.getApplicationContext<Context>()) {
        TestTransferDatabase(
                FavoriteUsersSQLiteHelper.TABLE_HOME,
                "favorite_users",
                FavoriteUserTransfer(this),
                sourceColumns,
                FavoriteUsersSQLiteHelper.COLUMN_ID
        )
    }

    @Test
    @Throws(Exception::class)
    fun basicFavoriteUserTransfer() {
        val mockFavoriteUser = MockTransferFavoriteUser(1)

        val oldId = database.insertIntoSQLiteDatabase(mockFavoriteUser)
        assertThat("Failed insertion into database", oldId, not(equalTo(-1L)))
        assertThat("Failed insertion into source SQLite database", database.sourceSize, equalTo(1))

        database.buildDestinationDatabase()

        assertThat("Entity did not transfer into the new database", database.destSize, equalTo(1))
        val favoriteUser = database.queryFromTalonDatabase("SELECT * FROM favorite_users LIMIT 1")!!.use { cursor ->
            cursor.moveToFirst()
            MockFavoriteUser(cursor)
        }

        val expected = mockFavoriteUser.copyId(favoriteUser.id).mockEntity
        assertThat("Entities are different", expected, matchesMockEntity(favoriteUser))

    }

}