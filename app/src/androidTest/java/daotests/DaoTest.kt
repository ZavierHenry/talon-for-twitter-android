package daotests


import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

import com.klinker.android.twitter_l.data.roomdb.TalonDatabase

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.klinker.android.twitter_l.data.roomdb.entities.Tweet
import com.klinker.android.twitter_l.data.roomdb.entities.User
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import java.lang.Exception




abstract class DaoTest {

    fun queryDatabase(query: String, args: Array<Any>?) : Cursor {
        return testDatabase.query(query, args)
    }

    fun deleteFromDatabase(table: String, where: String, args: Array<Any>?) : Int {
        return try {
            testDatabase.openHelper.writableDatabase.delete(table, where, args)
        } catch (_: Exception) {
            0
        }
    }

    fun insertIntoDatabase(tableName: String, conflictAlgorithm: Int, values: ContentValues): Long {
        return testDatabase.openHelper.writableDatabase.insert(tableName, conflictAlgorithm, values)
    }


    fun clearUnfinishedTransaction() {
        if (testDatabase.openHelper.writableDatabase.inTransaction()) {
            testDatabase.openHelper.writableDatabase.endTransaction()
        }
    }

    inline fun <R> asTransaction(block: () -> R) : R {
        testDatabase.openHelper.writableDatabase.beginTransaction()

        val result = block()

        testDatabase.openHelper.writableDatabase.setTransactionSuccessful()
        testDatabase.openHelper.writableDatabase.endTransaction()

        return result

    }

    fun clearTestDatabase() {
        testDatabase.clearAllTables()
    }

    fun setTweetContentValues(contentValues: ContentValues, tweet: Tweet) {
        with(contentValues) {
            put("id", tweet.id)
            put("text", tweet.text)
            put("user_id", tweet.userId)
            put("time", tweet.time)
            put("urls", tweet.urls)
            put("users", tweet.users)
            put("picture_urls", tweet.pictureUrls)
            put("retweeter", tweet.retweeter)
            put("gif_url", tweet.gifUrl)
            put("media_length", tweet.mediaLength)
            put("is_conversation", tweet.isConversation)
            put("like_count", tweet.likeCount)
            put("retweet_count", tweet.retweetCount)
            put("client_source", tweet.clientSource)
            put("hashtags", tweet.hashtags)
        }
    }

    fun setUserContentValues(contentValues: ContentValues, user: User) {
        with(contentValues) {
            put("id", user.id)
            put("twitter_id", user.twitterId)
            put("name", user.name)
            put("screen_name", user.screenName)
            put("is_verified", user.isVerified)
            put("profile_pic", user.profilePic)
        }
    }


    protected fun insertUser(user: User) : User? {
        val contentValues = ContentValues()
        setUserContentValues(contentValues, user)
        val id = insertIntoDatabase("users", SQLiteDatabase.CONFLICT_IGNORE, contentValues)
        return if (id != -1L) user.copy(id = id) else null
    }

    protected fun insertTweet(tweet: Tweet) : Tweet? {
        val contentValues = ContentValues()
        setTweetContentValues(contentValues, tweet)
        val id = insertIntoDatabase("tweets", SQLiteDatabase.CONFLICT_IGNORE, contentValues)
        return if (id != -1L) tweet else null
    }

    protected fun insertTweet(user: User, tweet: Tweet) : Pair<User, Tweet>? {
        return insertUser(user)?.let { u ->
            insertTweet(tweet.copy(userId = u.id!!))?.let { t ->
                u to t
            }
        }
    }


    fun cursorToUser(cursor: Cursor) : User {

        return with(cursor) {
            val id = getColumnIndex("id").let { if (it != -1) getLong(it) else null}
            val twitterId = getColumnIndex("twitter_id").let { if (it != -1) getLong(it) else null }
            val name = getColumnIndex("name").let { if (it != -1) getString(it) else ""}
            val screenName = getColumnIndex("screen_name").let { if (it != -1) getString(it) else "" }
            val profilePic = getColumnIndex("profile_pic").let { if (it != -1) getString(it) else "" }
            val isVerified = getColumnIndex("is_verified").let { it != -1 && getInt(it) == 1 }
            User(id, twitterId, name, screenName, profilePic, isVerified)
        }

    }


    protected fun makeUser(id: Long? = null, twitterId: Long? = null, prefix: String = "User") : User {
        return User(id, twitterId, "$prefix $id", "$prefix$id", "https;//twitter.com/$prefix/img/$id/img.png", false)
    }


    //TODO: make template trim database trims database test
    protected inline fun runTrimDatabaseTrimsDatabaseTest(table: String, account: Int, toContentValues: (account: Int, index: Int) -> ContentValues, trimFunction: (account: Int, trimSize: Int) -> Unit) {
        val databaseSize = 5
        val trimSize = 3

        val idCount = (0 until databaseSize).count {
            insertIntoDatabase(table, SQLiteDatabase.CONFLICT_IGNORE, toContentValues(account, it)) != -1L
        }

        assertThat("There must be more than $trimSize entities in table $table to properly run this test", idCount, greaterThan(trimSize))
        trimFunction(account, trimSize)

        queryDatabase("SELECT 1 FROM $table", null).use { cursor ->
            assertThat("Incorrect number of entries in table $table", cursor.count, `is`(trimSize))
        }
    }



    //TODO: make template trim database ignores small database test
    protected inline fun runTrimDatabaseDoesntTrimSmallDatabase(table: String, account: Int, toContentValues: (account: Int, index: Int) -> ContentValues, trimFunction: (account: Int, trimSize: Int) -> Unit) {
        val databaseSize = 3
        val trimSize = 5

        val idCount = (0 until databaseSize).count {
            insertIntoDatabase(table, SQLiteDatabase.CONFLICT_IGNORE, toContentValues(account, it)) != -1L
        }

        assertThat("There must be at least one entity in table $table to properly run this test", idCount, greaterThan(0))
        trimFunction(account, trimSize)

        queryDatabase("SELECT 1 FROM $table", null).use { cursor ->
            assertThat("Incorrect number of entries in table $table", cursor.count, `is`(idCount))
        }

    }


    //TODO: make template trim database ignores other accounts test
    protected inline fun runTrimDatabaseIgnoresOtherAccounts(table: String, account: Int, toContentValues: (account: Int, index: Int) -> ContentValues, trimFunction: (account: Int, trimSize: Int) -> Unit) {
        val otherAccount = account + 1
        val databaseSize = 5
        val trimSize = 3

        val idCount = (0 until databaseSize).count {
            insertIntoDatabase(table, SQLiteDatabase.CONFLICT_IGNORE, toContentValues(account, it)) != -1L
        }

        assertThat("There must be more than $trimSize entities in table $table to run this test properly", idCount, greaterThan(trimSize))
        trimFunction(otherAccount, trimSize)

        queryDatabase("SELECT 1 FROM $table", null).use { cursor ->
            assertThat("Incorrect number of entities in table $table", cursor.count, `is`(idCount))
        }

    }



    //TODO: make template user deletion restriction test
    protected inline fun runRestrictUserDeletionTest(tableName: String, userId: Long? = null, twitterId: Long = 1, toRestrictType: (User) -> ContentValues) {

        val user = insertUser(User(userId, twitterId, "User $twitterId", "user$twitterId", "https://twitter.com/img/$twitterId/img.png", false))!!
        val id = insertIntoDatabase(tableName, SQLiteDatabase.CONFLICT_IGNORE, toRestrictType(user))

        assertThat("Entity must be in table $tableName to run this test properly", id, not(-1L))

        val deletedCount = deleteFromDatabase("users", "id = ?", arrayOf(user.id!!))
        assertThat("User deletion was not restricted by the restriction in table $tableName", deletedCount, `is`(0))

    }


    //TODO: make template tweet deletion restriction test
    protected inline fun runRestrictTweetDeletionTest(tableName: String, userId : Long? = null, twitterId: Long = 1, tweetId: Long = 1L, toRestrictType: (User, Tweet) -> ContentValues) {

        val user = insertUser(User(userId, twitterId, "User $tweetId", "user$tweetId", "https://twitter.com/img/$tweetId/img.png", false))!!
        val tweet = insertTweet(Tweet(tweetId, text = "Sample text", userId = user.id!!))!!
        val id = insertIntoDatabase(tableName, SQLiteDatabase.CONFLICT_IGNORE, toRestrictType(user, tweet))

        assertThat("Entity must be in table $tableName to run this test properly", id, not(-1L))

        val deletedTweets = deleteFromDatabase("tweets", "id = ?", arrayOf(tweetId))
        assertThat("Tweet deletion was not restricted by the restriction in table $tableName", deletedTweets, `is`(0))

    }


    //TODO: make template reject duplicate test


    //TODO: make template delete all entries from an account
    protected inline fun runDeleteAllEntriesFromAccount(table: String, account: Int, toRestrictType: (account: Int, index: Int) -> ContentValues, deletionFunction: (Int) -> Unit, accountColumn: String = "account") {
        val databaseSize = 5

        val idCount = (0 until databaseSize).count {
            insertIntoDatabase(table, SQLiteDatabase.CONFLICT_IGNORE, toRestrictType(account, it)) != -1L
        }

        assertThat("There must be at least one entity in table $table to run this test properly", idCount, greaterThan(0))

        deletionFunction(account)

        queryDatabase("SELECT 1 FROM $table WHERE $accountColumn = ?", arrayOf(account)).use { cursor ->
            assertThat("Incorrect number of entity in $table of account $account", cursor.count, `is`(0))
        }

    }


    //TODO: make template get entities change pageSize

    //TODO: make template get entities in specified order

    //TODO: make template get entities change page

    //TODO: make template delete entity





    //TODO: make template insert entity



    //TODO: make cursor to tweet function



    companion object {
        lateinit var testDatabase: TalonDatabase

        @JvmStatic internal fun initTestDatabase() {
            val context = InstrumentationRegistry.getInstrumentation().targetContext
            testDatabase = Room.inMemoryDatabaseBuilder(context, TalonDatabase::class.java).build()
        }

        @JvmStatic internal fun closeTestDatabase() {
            testDatabase.close()
        }
    }

}
