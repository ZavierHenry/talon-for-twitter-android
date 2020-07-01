package com.klinker.android.twitter_l.transfertests

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.klinker.android.twitter_l.data.roomdb.transfers.HomeTweetTransfer
import com.klinker.android.twitter_l.data.sq_lite.HomeSQLiteHelper
import com.klinker.android.twitter_l.mockentities.MockHomeTweet
import com.klinker.android.twitter_l.mockentities.MockUtilities
import com.klinker.android.twitter_l.mockentities.matchers.MockEntityMatcher.Companion.matchesMockEntity
import com.klinker.android.twitter_l.mockentities.transferentities.MockTransferHomeTweet
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class HomeTweetTransferDatabaseTest {

    private val sourceColumns = listOf(
            "${HomeSQLiteHelper.COLUMN_ID} integer primary key",
            "${HomeSQLiteHelper.COLUMN_ACCOUNT} integer",
            "${HomeSQLiteHelper.COLUMN_TWEET_ID} integer",
            "${HomeSQLiteHelper.COLUMN_UNREAD} integer",
            "${HomeSQLiteHelper.COLUMN_TYPE} integer",
            "${HomeSQLiteHelper.COLUMN_ARTICLE} text",
            "${HomeSQLiteHelper.COLUMN_TEXT} text not null",
            "${HomeSQLiteHelper.COLUMN_NAME} text",
            "${HomeSQLiteHelper.COLUMN_PRO_PIC} text",
            "${HomeSQLiteHelper.COLUMN_SCREEN_NAME} text",
            "${HomeSQLiteHelper.COLUMN_TIME} integer",
            "${HomeSQLiteHelper.COLUMN_URL} text",
            "${HomeSQLiteHelper.COLUMN_PIC_URL} text",
            "${HomeSQLiteHelper.COLUMN_HASHTAGS} text",
            "${HomeSQLiteHelper.COLUMN_USERS} text",
            "${HomeSQLiteHelper.COLUMN_RETWEETER} text",
            "${HomeSQLiteHelper.COLUMN_ANIMATED_GIF} text",
            "${HomeSQLiteHelper.COLUMN_CURRENT_POS} text",
            "${HomeSQLiteHelper.COLUMN_CLIENT_SOURCE} text",
            "${HomeSQLiteHelper.COLUMN_CONVERSATION} integer default 0",
            "${HomeSQLiteHelper.COLUMN_MEDIA_LENGTH} integer default -1"
    )

    @get:Rule val database = with(ApplicationProvider.getApplicationContext<Context>()) {
        TestTransferDatabase(
                HomeSQLiteHelper.TABLE_HOME,
                "home_tweets",
                HomeTweetTransfer(this),
                sourceColumns,
                HomeSQLiteHelper.COLUMN_ID
        )
    }

    @Test
    @Throws(Exception::class)
    fun testBasicHomeTweetTransfer() {
        val tweet = MockUtilities.makeMockTweet(
                likes = null,
                retweets = null,
                retweeted = null,
                liked = null,
                author = MockUtilities.makeMockUser(userId = null)
        )
        val mockTransferHomeTweet = MockTransferHomeTweet(1, tweet = tweet)

        val oldId = database.insertIntoSQLiteDatabase(mockTransferHomeTweet)
        assertThat("Home tweet did not enter into initial database", oldId, not(equalTo(-1L)))
        assertThat("Entity is not in SQLite database", database.sourceSize, equalTo(1))

        database.buildDestinationDatabase()

        assertThat("Entity did not transfer into new database", database.destSize, equalTo(1))
        val mockHomeTweet = database.queryFromTalonDatabase("SELECT * FROM home_tweets LIMIT 1")!!.use { cursor ->
            cursor.moveToFirst()
            MockHomeTweet(cursor)
        }

        val expected = mockTransferHomeTweet.copyId(mockHomeTweet.id).mockEntity
        assertThat("Entities are not equal", expected, matchesMockEntity(mockHomeTweet))


    }

}