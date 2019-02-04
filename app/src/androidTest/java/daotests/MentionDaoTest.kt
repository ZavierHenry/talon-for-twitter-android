package daotests

import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase

import com.klinker.android.twitter_l.data.roomdb.TalonDatabase

import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.klinker.android.twitter_l.data.roomdb.daos.MentionDao
import com.klinker.android.twitter_l.data.roomdb.entities.Mention
import com.klinker.android.twitter_l.data.roomdb.entities.Tweet
import com.klinker.android.twitter_l.data.roomdb.entities.User
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*

class MentionDaoTest : DaoTest() {

    private val mentionDao get() = testDatabase.mentionDao()
    private val sharedPreferences get() =
        InstrumentationRegistry.getInstrumentation().targetContext.getSharedPreferences(MentionDaoTest.preferenceName, Context.MODE_PRIVATE)


    @Test
    fun insertMentionTest() {

    }

    @Test
    fun insertMentionWithConflict() {

    }

    @Test
    fun deleteMention() {

    }

    @Test
    fun getLatestScreenName() {

    }

    @Test
    fun getLatestUsers() {


    }



    @Test
    fun deleteAllMentions() {
        val userContentValues = ContentValues()
        val tweetContentValues = ContentValues()
        val mentionContentValues = ContentValues()

        val account = 1

        val mentionIdCount = (0..2).map {
            setUserContentValues(userContentValues, User(null, it.toLong(), "User $it", "user$it", "https://twitter.com/img/$it/img.png", false))
            insertIntoDatabase("users", SQLiteDatabase.CONFLICT_IGNORE, userContentValues)
        }.filterNot { it == -1L }.mapIndexed { index, userId ->
            setTweetContentValues(tweetContentValues, Tweet(id = index.toLong() + 88, text = "Sample text $index", userId = userId))
            insertIntoDatabase("tweets", SQLiteDatabase.CONFLICT_IGNORE, tweetContentValues)
        }.filterNot { it == -1L }.map { tweetId ->
            setMentionContentValues(mentionContentValues, Mention(null, account, tweetId, false))
            insertIntoDatabase("mentions", SQLiteDatabase.CONFLICT_IGNORE, mentionContentValues)
        }.count { it != -1L }

        assertThat("At least one mention must be in the database for this test to run properly", mentionIdCount, greaterThan(0))
        mentionDao.deleteAllMentions(account)
        queryDatabase("SELECT id FROM mentions", null).use { cursor ->
            assertThat("Incorrect number of mentions in the database", cursor.count, `is`(0))
        }
    }


    @Test
    fun deleteAllMentionsIgnoresOtherAccounts() {
        val userContentValues = ContentValues()
        val tweetContentValues = ContentValues()
        val mentionContentValues = ContentValues()

        val account = 1
        val otherAccount = account + 1

        val mentionIdCount = (0..2).map {
            setUserContentValues(userContentValues, User(null, it.toLong(), "User $it", "user$it", "https://twitter.com/img/$it/img.png", false))
            insertIntoDatabase("users", SQLiteDatabase.CONFLICT_IGNORE, userContentValues)
        }.filterNot { it == -1L }.mapIndexed { index, userId ->
            setTweetContentValues(tweetContentValues, Tweet(id = index.toLong() + 88, text = "Sample text $index", userId = userId))
            insertIntoDatabase("tweets", SQLiteDatabase.CONFLICT_IGNORE, tweetContentValues)
        }.filterNot { it == -1L }.map { tweetId ->
            setMentionContentValues(mentionContentValues, Mention(null, account, tweetId, true))
            insertIntoDatabase("mentions", SQLiteDatabase.CONFLICT_IGNORE, mentionContentValues)
        }.count { it != -1L }

        assertThat("There needs to be at least one mention in the database to run this test properly", mentionIdCount, greaterThan(0))
        mentionDao.deleteAllMentions(otherAccount)

        queryDatabase("SELECT id FROM mentions", null).use { cursor ->
            assertThat("Incorrect number of mentions", cursor.count, `is`(mentionIdCount))
        }

    }


    @Test
    fun markAllRead() {
        val userContentValues = ContentValues()
        val tweetContentValues = ContentValues()
        val mentionContentValues = ContentValues()

        val account = 1

        val mentionIdCount = (0..2).map {
            setUserContentValues(userContentValues, User(null, it.toLong(), "User $it", "user$it", "https://twitter.com/img/$it/img.png", false))
            insertIntoDatabase("users", SQLiteDatabase.CONFLICT_IGNORE, userContentValues)
        }.filterNot { it == -1L }.mapIndexed { index, userId ->
            setTweetContentValues(tweetContentValues, Tweet(id = index.toLong() + 99, text = "Sample text $index", userId = userId))
            insertIntoDatabase("tweets", SQLiteDatabase.CONFLICT_IGNORE, tweetContentValues)
        }.filterNot { it == -1L }.map { tweetId ->
            setMentionContentValues(mentionContentValues, Mention(null, account, tweetId, true))
            insertIntoDatabase("mentions", SQLiteDatabase.CONFLICT_IGNORE, mentionContentValues)
        }.count { it != -1L }

        assertThat("At least one mention must be in the database for this test to run properly", mentionIdCount, greaterThan(0))

        mentionDao.markAllRead(account)
        queryDatabase("SELECT id FROM mentions WHERE account = ? AND is_unread = 1", arrayOf(account)).use { cursor ->
            assertThat("Not all mentions were marked as read", cursor.count, `is`(0))
        }

    }


    @Test
    fun markAllReadIgnoresOtherAccounts() {
        val userContentValues = ContentValues()
        val tweetContentValues = ContentValues()
        val mentionContentValues = ContentValues()

        val account = 1
        val otherAccount = account + 1

        val mentionIdCount = (0..2).map {
            setUserContentValues(userContentValues, User(null, it.toLong(), "User $it", "user$it", "https://twitter.com/img/$it/img.png", false))
            insertIntoDatabase("users", SQLiteDatabase.CONFLICT_IGNORE, userContentValues)
        }.filterNot { it == -1L }.mapIndexed { index, userId ->
            setTweetContentValues(tweetContentValues, Tweet(id = index.toLong() + 99, text = "Sample text $index", userId = userId))
            insertIntoDatabase("tweets", SQLiteDatabase.CONFLICT_IGNORE, tweetContentValues)
        }.filterNot { it == -1L }.map { tweetId ->
            setMentionContentValues(mentionContentValues, Mention(null, account, tweetId, true))
            insertIntoDatabase("mentions", SQLiteDatabase.CONFLICT_IGNORE, mentionContentValues)
        }.count { it != -1L }

        assertThat("There must be at least one mention in the database to run this test properly", mentionIdCount, greaterThan(0))
        mentionDao.markAllRead(otherAccount)

        queryDatabase("SELECT id FROM mentions WHERE is_unread = 0", null).use { cursor ->
            assertThat("Mentions from the wrong account were marked as read", cursor.count, `is`(0))
        }

    }



    @Test
    fun getUnreadCountWithoutMutes() {
        val userContentValues = ContentValues()
        val tweetContentValues = ContentValues()
        val mentionContentValues = ContentValues()

        val account = 1

        val mentionIdCount = (0..1).map {
            setUserContentValues(userContentValues, User(null, it.toLong(), "User $it", "user$it", "https://twitter.com/img/$it/img.png", false))
            insertIntoDatabase("users", SQLiteDatabase.CONFLICT_IGNORE, userContentValues)
        }.filterNot { it == -1L }.mapIndexed { index, userId ->
            setTweetContentValues(tweetContentValues, Tweet(id = index.toLong() + 88, text = "Sample text $index", userId = userId))
            insertIntoDatabase("tweets", SQLiteDatabase.CONFLICT_IGNORE, tweetContentValues)
        }.filterNot { it == -1L }.map { tweetId ->
            setMentionContentValues(mentionContentValues, Mention(null, account, tweetId, true))
            insertIntoDatabase("mentions", SQLiteDatabase.CONFLICT_IGNORE, mentionContentValues)
        }.count { it != -1L }

        assertThat("At least one unread mention must be in this database to test properly", mentionIdCount, greaterThan(0))

        val unreadCount = mentionDao.getUnreadCount(account, true)
        assertThat("Incorrect unread count", unreadCount, `is`(mentionIdCount))

    }

    @Test
    fun getUnreadCountWithoutMutesIgnoresOtherAccount() {

        val userContentValues = ContentValues()
        val tweetContentValues = ContentValues()
        val mentionContentValues = ContentValues()

        val account = 1
        val otherAccount = account + 1

        val mentionIdCount = (0..2).map {
            setUserContentValues(userContentValues, User(null, it.toLong(), "User $it", "user$it", "https://twitter.com/img/$it/img.png", false))
            insertIntoDatabase("users", SQLiteDatabase.CONFLICT_IGNORE, userContentValues)
        }.filterNot { it == -1L }.mapIndexed { index, userId ->
            setTweetContentValues(tweetContentValues, Tweet(id = index.toLong() + 88, text= "Sample Text $index", userId = userId))
            insertIntoDatabase("tweets", SQLiteDatabase.CONFLICT_IGNORE, tweetContentValues)
        }.filterNot { it == -1L }.map { tweetId ->
            setMentionContentValues(mentionContentValues, Mention(null, account, tweetId, true))
            insertIntoDatabase("mentions", SQLiteDatabase.CONFLICT_IGNORE, mentionContentValues)
        }.count { it != -1L }

        assertThat("At least one mention must be in the database", mentionIdCount, greaterThan(0))
        val unreadCount = mentionDao.getUnreadCount(otherAccount, true)

        assertThat("Incorrect number of unread mentions returned", unreadCount, `is`(0))

    }

    @Test
    fun getUnreadCountWithoutMutesDoesntIncludeReadMentions() {

        val userContentValues = ContentValues()
        val tweetContentValues = ContentValues()
        val mentionContentValues = ContentValues()

        val account = 1

        val mentionIdCount = (0..1).map {
            setUserContentValues(userContentValues, User(null, it.toLong(), "User $it", "user$it", "https://twitter.com/img/$it/img.png", false))
            insertIntoDatabase("users", SQLiteDatabase.CONFLICT_IGNORE, userContentValues)
        }.filterNot { it == -1L }.mapIndexed { index, userId ->
            setTweetContentValues(tweetContentValues, Tweet(index.toLong() + 88, text = "Sample text $index", userId = userId))
            insertIntoDatabase("tweets", SQLiteDatabase.CONFLICT_IGNORE, tweetContentValues)
        }.filterNot { it == -1L }.map { tweetId ->
            setMentionContentValues(mentionContentValues, Mention(null, account, tweetId, false))
            insertIntoDatabase("mentions", SQLiteDatabase.CONFLICT_IGNORE, mentionContentValues)
        }.count { it != -1L }

        assertThat("At least mention must be in the database for this test to run properly", mentionIdCount, greaterThan(0))
        val unreadCount = mentionDao.getUnreadCount(account, true)

        assertThat("Incorrect number of unread mentions", unreadCount, `is`(0))

    }


    @Test
    fun getUnreadCountWithoutMutesEmpty() {

        val unreadCount = mentionDao.getUnreadCount(1, true)
        assertThat("Incorrect number of unread mentions", unreadCount, `is`(0))

    }



    @Test
    fun getUnreadCountWithMutesNoneFiltered() {



    }


    @Test
    fun getUnreadCountWithMutesTextFiltered() {

    }

    @Test
    fun getUnreadCountWithMutesScreenNameFiltered() {

    }

    @Test
    fun getUnreadCountWithMutesHashtagsFiltered() {

    }

    @Test
    fun getUnreadCountWithMutesTextCaseFiltered() {

    }

    @Test
    fun getUnreadCountWithMutesHashtagsCaseFiltered() {

    }


    @Test
    fun getUnreadCountWithMutesPartialStringNotFiltered() {

    }


    @Test
    fun getLatestPictureUrlsWithoutMutes() {
        val userContentValues = ContentValues()
        val tweetContentValues = ContentValues()
        val mentionContentValues = ContentValues()

        val account = 1

        val picUrls = (0..2).map {
            setUserContentValues(userContentValues, User(null, it.toLong(), "User $it", "user$it", "https://twitter.com/img/$it/img.png", false))
            insertIntoDatabase("users", SQLiteDatabase.CONFLICT_IGNORE, userContentValues)
        }.filterNot { it == -1L }.mapIndexed { index, userId ->
            val picUrls = "https://twitter.com/purl/$index/pic.png"
            setTweetContentValues(tweetContentValues, Tweet(id = index.toLong() + 88, text = "Sample text $index", userId = userId, pictureUrls = picUrls))
            val id = insertIntoDatabase("tweets", SQLiteDatabase.CONFLICT_IGNORE, tweetContentValues)
            id to picUrls
        }.filterNot { (id, _) -> id == -1L }.sortedByDescending { (id, _) -> id }.map { (tweetId, picUrls) ->
            setMentionContentValues(mentionContentValues,  Mention(null, account, tweetId, tweetId % 2 == 0L))
            val id = insertIntoDatabase("mentions", SQLiteDatabase.CONFLICT_IGNORE, mentionContentValues)
            id to picUrls
        }.filterNot { (id, _) -> id == -1L }.map { (_, picUrls) -> picUrls }


        assertThat("At least one mention must be in the database to properly run this test", picUrls, hasSize(greaterThan(0)))
        val latestPictureUrl = mentionDao.getLatestPictureUrls(account, true)

        assertThat("Incorrect picture urls", latestPictureUrl, `is`(picUrls.first()))

    }

    @Test
    fun getLatestPictureUrlsWithoutMutesIgnoresOtherAccount() {
        val userContentValues = ContentValues()
        val tweetContentValues = ContentValues()
        val mentionContentValues = ContentValues()

        val account = 1
        val otherAccount = account + 1




    }



    @Test
    fun getLatestPictureUrlsWithoutMutesEmpty() {
        val account = 1

        val latestPicUrl = mentionDao.getLatestPictureUrls(account, true)
        assertThat("Should return null", latestPicUrl, nullValue())

    }



    @Test
    fun getLatestTweetIds() {

        val userContentValues = ContentValues()
        val tweetContentValues = ContentValues()
        val mentionContentValues = ContentValues()
        val account = 1

        val testSize = 3

        val mentionTweetIds = (testSize downTo 1).map {
            setUserContentValues(userContentValues, User(null, it.toLong(), "User $it", "user$it", "https://twitter.com/img/$it/img.png", false))
            insertIntoDatabase("users", SQLiteDatabase.CONFLICT_IGNORE, userContentValues)
            }.filterNot { it == -1L }.mapIndexed { index, userId ->
                setTweetContentValues(tweetContentValues, Tweet(id = index.toLong() + 88, userId = userId, text = "Unique test $index"))
                insertIntoDatabase("tweets", SQLiteDatabase.CONFLICT_IGNORE, tweetContentValues)
            }.filterNot { it == -1L }.map { tweetId ->
                setMentionContentValues(mentionContentValues, Mention(null, account, tweetId, true))
                val mentionId = insertIntoDatabase("mentions", SQLiteDatabase.CONFLICT_IGNORE, mentionContentValues)
                mentionId to tweetId
            }.filterNot { (mentionId, _) -> mentionId == -1L }.sortedByDescending { (mentionId, _) -> mentionId }.map { (_, tweetId) -> tweetId }

        assertThat("There must be $testSize mentions in the database to run this test properly", mentionTweetIds, hasSize(testSize))
        val ids = mentionDao.getLatestTweetIds(account, testSize - 1)

        assertThat("Incorrect set of ids returned", ids, contains(mentionTweetIds.dropLast(1).map { `is`(it) }))

    }

    @Test
    fun getLatestTweetIdsWithSmallerDatabase() {
        val databaseSize = 2
        val latestTweetIdSize = 4
        val account = 1

        val userContentValues = ContentValues()
        val tweetContentValues = ContentValues()
        val mentionContentValues = ContentValues()

        val mentionTweetIds = (databaseSize downTo 1).map {
            setUserContentValues(userContentValues, User(null, it.toLong(), "User $it", "user$it", "https://twitter.com/img/$it/img.png", false))
            insertIntoDatabase("users", SQLiteDatabase.CONFLICT_IGNORE, userContentValues)
        }.filterNot { it == -1L }.mapIndexed { index, userId ->
            setTweetContentValues(tweetContentValues, Tweet(id = index.toLong() + 88, userId = userId, text = "Unique test $index"))
            insertIntoDatabase("tweets", SQLiteDatabase.CONFLICT_IGNORE, tweetContentValues)
        }.filterNot { it == -1L }.map { tweetId ->
            setMentionContentValues(mentionContentValues, Mention(null, account, tweetId, true))
            val mentionId = insertIntoDatabase("mentions", SQLiteDatabase.CONFLICT_IGNORE, mentionContentValues)
            mentionId to tweetId
        }.filterNot { (mentionId, _) -> mentionId == -1L }.sortedByDescending { (mentionId, _) -> mentionId }.map { (_, tweetId) -> tweetId }

        assertThat("There needs to be at least one element in the database to properly run this test", mentionTweetIds, hasSize(greaterThan(0)))

        val latestTweetId = mentionDao.getLatestTweetIds(account, latestTweetIdSize)
        assertThat("Latest tweet ids did not properly query from the database", latestTweetId, contains(mentionTweetIds.map { `is`(it) }))

    }


    @Test
    fun getLatestTweetIdsIgnoresOtherAccounts() {

        val databaseSize = 3

        val account = 1
        val otherAccount = 2

        val userContentValues = ContentValues()
        val tweetContentValues = ContentValues()
        val mentionContentValues = ContentValues()

        val tweetIds = (0..databaseSize).map {
            setUserContentValues(userContentValues, User(null, it.toLong(), "User $it", "user$it", "https://twitter.com/img/$it/img.png", false))
            insertIntoDatabase("users", SQLiteDatabase.CONFLICT_IGNORE, userContentValues)
        }.filterNot { it == -1L }.mapIndexed { index, userId ->
            setTweetContentValues(tweetContentValues, Tweet(id = index.toLong() + 88, userId = userId, text = "Sample text $index"))
            insertIntoDatabase("tweets", SQLiteDatabase.CONFLICT_IGNORE, tweetContentValues)
        }.filterNot { it == -1L }.map{ tweetId ->
            setMentionContentValues(mentionContentValues, Mention(null, account, tweetId, true))
            insertIntoDatabase("mentions", SQLiteDatabase.CONFLICT_IGNORE, mentionContentValues)
        }

        assertThat("There needs to be at least one value in the database to properly run this test", tweetIds, hasSize(greaterThan(0)))

        val latestTweetIds = mentionDao.getLatestTweetIds(otherAccount, tweetIds.size)
        assertThat("Latest ids included ids from account $account", latestTweetIds, empty())

    }


    @Test
    fun trimDatabaseTrimsDatabase() {
        val databaseSize = 5
        val trimDatabaseSize = 3
        val account = 1


        val userContentValues = ContentValues()
        val tweetContentValues = ContentValues()
        val mentionContentValues = ContentValues()

        val mentionIdCount = asTransaction {

            (0 until databaseSize).map {
                setUserContentValues(userContentValues, User(null, it.toLong(), "User $it", "user$it", "twitter.com/$it/img.png", false))
                insertIntoDatabase("users", SQLiteDatabase.CONFLICT_IGNORE, userContentValues)
            }.filterNot { it == -1L }.mapIndexed { index, userId ->
                setTweetContentValues(tweetContentValues, Tweet(id = index.toLong(), text = "Hello man", time = 5434, userId = userId))
                insertIntoDatabase("tweets", SQLiteDatabase.CONFLICT_IGNORE, tweetContentValues)
            }.filterNot { it == -1L }.map { tweetId ->
                setMentionContentValues(mentionContentValues, Mention(null, account, tweetId, true))
                insertIntoDatabase("mentions", SQLiteDatabase.CONFLICT_IGNORE, mentionContentValues)
            }.count { it != -1L }

        }

        assertThat("More than $trimDatabaseSize must be in the database for this test to work properly", mentionIdCount, greaterThan(trimDatabaseSize))
        mentionDao.trimDatabase(account, trimDatabaseSize)

        queryDatabase("SELECT id FROM mentions", null).use { cursor ->
            assertThat("Database count is not the trim size", cursor.count, `is`(trimDatabaseSize))
        }

    }

    @Test
    fun trimDatabaseDoesNotTrimSmallDatabase() {
        val databaseSize = 3
        val trimDatabaseSize = 5
        val account = 1

        val userContentValues = ContentValues()
        val tweetContentValues = ContentValues()
        val mentionContentValues = ContentValues()

        val mentionIdCount = asTransaction {

            (0 until databaseSize).map {
                setUserContentValues(userContentValues, User(null, it.toLong(), "User $it", "user$it", "https://twitter.com/$it/img.png", false))
                insertIntoDatabase("users", SQLiteDatabase.CONFLICT_IGNORE, userContentValues)
            }.filterNot { it == -1L }.mapIndexed { index, userId ->
                setTweetContentValues(tweetContentValues, Tweet(id = index.toLong(), text = "Hello man", time = 5434, userId = userId))
                insertIntoDatabase("tweets", SQLiteDatabase.CONFLICT_IGNORE, tweetContentValues)
            }.filterNot { it == -1L }.map { tweetId ->
                setMentionContentValues(mentionContentValues, Mention(null, account, tweetId, true))
                insertIntoDatabase("mentions", SQLiteDatabase.CONFLICT_IGNORE, mentionContentValues)
            }.count { it != -1L }

        }

        assertThat("At least one item must be in the database for this test to work properly", mentionIdCount, greaterThan(0))
        mentionDao.trimDatabase(account, trimDatabaseSize)

        queryDatabase("SELECT id FROM mentions", null).use { cursor ->
            assertThat("Values were deleted from the database", cursor.count, `is`(mentionIdCount))
        }

    }


    @Test
    fun trimDatabaseOnlyTrimsOneAccount() {
        val otherAccountSize = 5
        val trimDatabaseSize = 3
        val account = 1
        val otherAccount = 2

        val userContentValues = ContentValues()
        val tweetContentValues = ContentValues()
        val mentionContentValues = ContentValues()

        val otherAccountMentionCount = (0 until otherAccountSize + trimDatabaseSize).map {
            setUserContentValues(userContentValues, User(null, it.toLong(), "User $it", "user$it", "https://twitter.com/img/$it/img.png", false))
            insertIntoDatabase("users", SQLiteDatabase.CONFLICT_IGNORE, userContentValues)
        }.filterNot { it == -1L }.mapIndexed { index, userId ->
            setTweetContentValues(tweetContentValues, Tweet(id = index.toLong(), text = "Hello man", time = 5434, userId = userId))
            insertIntoDatabase("tweets", SQLiteDatabase.CONFLICT_IGNORE, tweetContentValues)
        }.filterNot { it == -1L }.mapIndexed { index, tweetId ->
            setMentionContentValues(mentionContentValues, Mention(null, if (index >= otherAccountSize) account else otherAccount, tweetId, true))
            insertIntoDatabase("mentions", SQLiteDatabase.CONFLICT_IGNORE, mentionContentValues) to index
        }.count { (id, index) ->  id != -1L && index < otherAccountSize}

        assertThat("More than $trimDatabaseSize mention(s) for account $otherAccount must be present to run this test", otherAccountMentionCount, greaterThan(trimDatabaseSize))
        mentionDao.trimDatabase(account, trimDatabaseSize)

        queryDatabase("SELECT * FROM mentions WHERE account = ?", arrayOf(otherAccount)).use { cursor ->
            assertThat("Mentions were incorrectly deleted from account $otherAccount", cursor.count, `is`(otherAccountMentionCount))
        }


    }

    @Test
    fun mentionsRestrictsDeleteTweet() {

        val account = 1

        val userId = with(ContentValues()) {
            setUserContentValues(this, User(null, 1, "User 1", "user1", "https://twitter.com/img/i/img.png", false))
            insertIntoDatabase("users", SQLiteDatabase.CONFLICT_ABORT, this)
        }

        assertThat("Mention user failed to save to database", userId, not(-1L))

        val tweetId = with(ContentValues()) {
            setTweetContentValues(this, Tweet(id = 1, text = "Blah bhar", time = 442, userId = userId))
            insertIntoDatabase("tweets", SQLiteDatabase.CONFLICT_ABORT, this)
        }

        assertThat("Mention tweet failed to save to database", tweetId, not(-1L))

        val mentionId = with(ContentValues()) {
            setMentionContentValues(this, Mention(null, account, tweetId, true))
            insertIntoDatabase("mentions", SQLiteDatabase.CONFLICT_ABORT, this)
        }

        assertThat("Mention failed to save to database", tweetId, not(-1L))

        testDatabase.query("DELETE FROM tweets WHERE id = ?", arrayOf(tweetId))

        queryDatabase("SELECT * FROM tweets", null).use { cursor ->
            assertThat("Mention did not restrict the tweet from being deleted", cursor.count, `is`(1))
        }

    }

    @After
    fun endUnfinishedTransaction() {
        clearUnfinishedTransaction()
    }

    @After
    fun clearTables() {
        clearTestDatabase()
    }

    @After
    fun clearPreferences() {
        sharedPreferences.edit().clear().commit()
    }


    private fun setMentionContentValues(contentValues: ContentValues, mention: Mention) {
        with(contentValues) {
            put("id", mention.id)
            put("account", mention.account)
            put("is_unread", mention.isUnread)
            put("tweet_id", mention.tweetId)
        }
    }






    companion object {

        const val preferenceName = "testPreferences"

        @BeforeClass
        @JvmStatic fun initDatabase() {
            DaoTest.initTestDatabase()
        }


        @AfterClass
        @JvmStatic fun closeDatabase() {
            DaoTest.closeTestDatabase()
        }


        @AfterClass
        @JvmStatic fun deleteSharedPreferences() {
            InstrumentationRegistry.getInstrumentation().targetContext.deleteSharedPreferences(preferenceName)
        }
    }
}
