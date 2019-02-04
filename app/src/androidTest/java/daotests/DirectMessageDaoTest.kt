package daotests

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.room.Dao
import com.klinker.android.twitter_l.data.roomdb.daos.DirectMessageDao
import com.klinker.android.twitter_l.data.roomdb.entities.DirectMessage
import com.klinker.android.twitter_l.data.roomdb.entities.User
import com.klinker.android.twitter_l.data.roomdb.pojos.DisplayDirectMessage
import matchers.DisplayDirectMessageMatcher
import matchers.DisplayDirectMessageMatcher.Companion.matchesDisplayDirectMessage
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.*

class DirectMessageDaoTest : DaoTest() {

    private val directMessageDao get() = testDatabase.directMessageDao()

    private fun toDirectMessageFunction(senderId: Long, recipientId: Long) : (account: Int, index: Int) -> ContentValues {
        return { account: Int, index: Int ->
            ContentValues().apply { setDirectMessageContentValues(this, DirectMessage(null, account = account, messageId = index.toLong() + 1, senderId = senderId, recipientId = recipientId)) }
        }
    }

    private val trimFunction = { account : Int, index : Int -> directMessageDao.trimDatabase(account, index) }
    private val deleteAllFunction = { account: Int -> directMessageDao.deleteAllDirectMessages(account) }


    @Test
    fun getAllDirectMessages() {
        val databaseSize = 5
        val account = 1

        val sender = insertUser(User(null, 1, "Sender", "sender", "https://twitter.com/img/1/img.png", false))!!
        val recipient = insertUser(User(null, 2, "Recipient", "recipient", "https://twitter.com/img/2/img.png", false))!!

        val senderId = sender.id!!
        val recipientId = recipient.id!!

        val directMessages = asTransaction {

            (0 until databaseSize).mapNotNull {
                insertDirectMessage(DirectMessage(null, account = account, senderId = senderId, recipientId = recipientId, messageId = it.toLong() + 1))
            }
        }

        assertThat("There must be at least two direct messages in the database to run this test properly", directMessages, hasSize(greaterThan(2)))

        val retrievedDirectMessages = directMessageDao.getAllDirectMessages(1)
        val matchers = directMessages.asSequence()
                .sortedBy { it.messageId }
                .map { matchesDisplayDirectMessage(DisplayDirectMessage(it, sender, recipient)) }
                .toList()

        assertThat("Incorrect list of direct messages", retrievedDirectMessages, contains(matchers))

    }

    @Test
    fun getAllDirectMessagesIgnoresOtherAccounts() {
        val account = 1
        val otherAccount = account + 1
        val accountDatabaseSize = 5
        val otherAccountDatabaseSize = 5

        val sender = insertUser(User(null, 1, "Sender", "sender", "https:/twitter.com/img/1/img.png", false))!!
        val recipient = insertUser(User(null, 2, "Recipient", "recipient", "https://twitter.com/img/2/img.png", false))!!

        val senderId = sender.id!!
        val recipientId = recipient.id!!

        val directMessages = asTransaction {
            (0 until accountDatabaseSize + otherAccountDatabaseSize).mapNotNull {
                insertDirectMessage(DirectMessage(null, account = if (it >= otherAccountDatabaseSize) account else otherAccount, senderId = senderId, recipientId = recipientId, messageId = it.toLong() + 1))
            }
        }

        assertThat("There must be at least one direct message of account $otherAccount in the database to run this test properly", directMessages.count { it.account == account }, greaterThan(0))

        val databaseDirectMessages = directMessageDao.getAllDirectMessages(account)
        val matchers = directMessages.asSequence()
                .filter { it.account == account }
                .sortedBy { it.messageId }
                .map { matchesDisplayDirectMessage(DisplayDirectMessage(it, sender, recipient)) }
                .toList()

        assertThat("Incorrect list of matchers", databaseDirectMessages, contains(matchers))

    }

    @Test
    fun getAllDirectMessagesLimitsPageSize() {
        val pageSize = 3
        val databaseSize = 5
        val account = 1

        val sender = insertUser(User(null, 1, "Sender", "sender", "https://twitter.com/img/1/img.png", false))!!
        val recipient = insertUser(User(null, 2, "Recipient", "recipient", "https://twitter.com/img/2/img.png", false))!!

        val senderId = sender.id!!
        val recipientId = recipient.id!!

        val directMessages = asTransaction {

            (0 until databaseSize).mapNotNull {
                insertDirectMessage(DirectMessage(null, account = account, senderId = senderId, recipientId = recipientId, messageId = it.toLong() + 1))
            }
        }

        assertThat("There must be more than $pageSize direct messages in the database for this test to run", directMessages, hasSize(greaterThan(pageSize)))

        val databaseDirectMessages = directMessageDao.getAllDirectMessages(account = 1, pageSize = pageSize)
        val matchers = directMessages.asSequence()
                .sortedBy { it.messageId }
                .take(pageSize)
                .map { matchesDisplayDirectMessage(DisplayDirectMessage(it, sender, recipient)) }
                .toList()

        assertThat("Incorrect list of direct messages", databaseDirectMessages, contains(matchers))

    }

    @Test
    fun getAllDirectMessagesUsesPageParameterProperly() {
        val page = 2
        val account = 1
        val pageSize = 3

        val sender = insertUser(User(null, 1, "Sender", "sender", "https://twitter.com/img/1/img.png", false))!!
        val recipient = insertUser(User(null, 2, "Recipient", "recipient", "https://twitter.com/img/2/img.png", false))!!

        val senderId = sender.id!!
        val recipientId = recipient.id!!

        val directMessages = asTransaction {
            (0 until (page+1) * pageSize).mapNotNull {
                insertDirectMessage(DirectMessage(null, account = account, senderId = senderId, recipientId = recipientId, messageId = it.toLong() + 1))
            }
        }

        assertThat("At least ${page * pageSize + 1} direct messages must be in the database to run this test properly", directMessages, hasSize(greaterThan(pageSize * page)))

        val databaseDirectMessages = directMessageDao.getAllDirectMessages(account, page = page, pageSize = pageSize)
        val matchers = directMessages
                .sortedBy { it.messageId }
                .subList((page - 1) * pageSize, page * pageSize)
                .map { matchesDisplayDirectMessage(DisplayDirectMessage(it, sender, recipient)) }


        assertThat("Incorrect list of direct messages", databaseDirectMessages, contains(matchers))

    }


    @Test
    fun getLatestDirectMessage() {
        val account = 1
        val databaseSize = 3

        val sender = insertUser(User(null, 1, "Sender", "sender", "https://twitter.com/img/1/img.png", false))!!
        val recipient = insertUser(User(null, 2, "Recipient", "recipient", "https://twitter.com/img/2/img.png", false))!!

        val senderId = sender.id!!
        val recipientId = recipient.id!!

        val directMessages = (0 until databaseSize).mapNotNull {
            insertDirectMessage(DirectMessage(null, account = account, senderId = senderId, recipientId = recipientId, messageId = it.toLong() + 1))
        }

        assertThat("At least one direct message must be in the database to run this test properly", directMessages, hasSize(greaterThan(0)))
        val latestDirectMessage = directMessageDao.getLatestDirectMessage(senderId, account)
        val expectedDirectMessage = DisplayDirectMessage(directMessages.asSequence().sortedByDescending { dm -> dm.messageId }.first(), sender, recipient)

        assertThat("Incorrect direct message is returned", latestDirectMessage, matchesDisplayDirectMessage(expectedDirectMessage))

    }

    @Test
    fun getLatestDirectMessageIgnoresOtherAccounts() {
        val account = 1
        val otherAccount = account + 1
        val accountDirectMessageSize = 3
        val messageId = 1L

        val sender = insertUser(User(null, 1, "Sender", "sender", "https://twitter.com/img/1/img.png", false))!!
        val recipient = insertUser(User(null, 2, "Recipient", "recipient", "https://twitter.com/img/2/img.png", false))!!

        val senderId = sender.id!!
        val recipientId = recipient.id!!

        val otherDirectMessage = insertDirectMessage(DirectMessage(null, account = otherAccount, senderId = senderId, recipientId = recipientId, messageId = messageId))!!
        val accountDirectMessagesCount = (0 until accountDirectMessageSize).count {
            insertDirectMessage(DirectMessage(null, account = account, senderId = senderId, recipientId = recipientId, messageId = messageId + it.toLong() + 1L)) != null
        }

        assertThat("At least one direct message for account $account needs to be in the database to run this test properly", accountDirectMessagesCount, greaterThan(0))

        val latestDirectMessasge = directMessageDao.getLatestDirectMessage(senderId, otherAccount)
        val expectedDirectMessage = DisplayDirectMessage(otherDirectMessage, sender, recipient)

        assertThat("Incorrect direct message was returned", latestDirectMessasge, matchesDisplayDirectMessage(expectedDirectMessage))

    }


    @Test
    fun getLatestDirectMessageIgnoresOtherSenders() {
        val account = 1
        val recipientDirectMessageSize = 3
        val messageId = 1L

        val sender = insertUser(User(null, 1, "Sender", "sender", "https://twitter.com/img/1/img.png", false))!!
        val recipient = insertUser(User(null, 2, "Recipient", "recipient", "https://twitter.com/img/2/img.png", false))!!

        val senderId = sender.id!!
        val recipientId = recipient.id!!

        val senderDirectMessage = insertDirectMessage(DirectMessage(null, account = account, senderId = senderId, recipientId = recipientId, messageId = messageId))!!
        val recipientDirectMessagesCount = (0 until recipientDirectMessageSize).count {
            insertDirectMessage(DirectMessage(null, account = account, senderId = recipientId, recipientId = senderId, messageId = messageId + it.toLong() + 1L)) != null
        }

        assertThat("At least one direct message for sender ${recipient.name} needs to be in the database to run this test properly", recipientDirectMessagesCount, greaterThan(0))

        val latestDirectMessage = directMessageDao.getLatestDirectMessage(senderId, account)
        val expectedDirectMessage = DisplayDirectMessage(senderDirectMessage, sender, recipient)

        assertThat("Incorrect direct message was returned", latestDirectMessage, matchesDisplayDirectMessage(expectedDirectMessage))

    }

    @Test
    fun getLatestDirectMessageReturnsNullIfEmptyTable() {
        val directMessage = directMessageDao.getLatestDirectMessage(1, 1)
        assertThat("Direct message is not null for some reason", directMessage, nullValue())
    }


    @Test
    fun deleteDirectMessageDeletesFromAllAccounts() {
        val account = 1
        val otherAccount = account + 1
        val messageId = 1L

        val firstDirectMessage = insertDirectMessage(
                sender = User(null, 1, "Sender", "sender", "https://twitter.com/img/1/img.png", false),
                recipient = User(null, 2, "Recipient", "recipient", "https://twitter.com/img/2/img.png", false),
                directMessage = DirectMessage(null, account = account, messageId = messageId)
        )

        assertThat("First direct message was not added to database", firstDirectMessage, notNullValue())

        val secondDirectMessage = insertDirectMessage(DirectMessage(null, account = otherAccount, messageId = messageId, senderId = firstDirectMessage!!.first.first.id!!, recipientId = firstDirectMessage.first.second.id!!))

        assertThat("Second direct message was not added to database", secondDirectMessage, notNullValue())

        directMessageDao.deleteDirectMessages(messageId)
        queryDatabase("SELECT id FROM direct_messages", null).use { cursor ->
            assertThat("Incorrect number of direct messages", cursor.count, `is`(0))
        }

    }


    @Test
    fun deleteDirectMessage() {
        val account = 1
        val messageId = 1L

        val results = insertDirectMessage(
                sender = User(null, 1, "Sender", "sender", "https://twitter.com/img/1/img.png", false),
                recipient = User(null, 2, "Recipient", "recipient", "https://twitter.com/img/2/img.png", false),
                directMessage = DirectMessage(null, account = account, messageId = messageId)
        )

        assertThat("Direct message must be in the database to run this test properly", results, notNullValue())
        directMessageDao.deleteDirectMessages(messageId)
        queryDatabase("SELECT id FROM direct_messages", null).use { cursor ->
            assertThat("Direct message did not delete properly", cursor.count, `is`(0))
        }

    }

    @Test
    fun deleteAllDirectMessages() {
        val account = 1
        val databaseSize = 3

        val dmIdCount = (0 until databaseSize).count {
            val sender = User(null, it.toLong(), "Sender $it", "sender$it", "https://twitter.com/img/$it/img.png", false)
            val recipient = User(null, it.toLong() + 50, "Recipient $it", "recipient$it", "https://twitter.com/img/$it/img.png", false)
            val directMessage = DirectMessage(null, account = account, messageId = it.toLong() + 1)
            insertDirectMessage(sender, recipient, directMessage) != null
        }

        assertThat("At least one direct message must be in the database to run this test properly", dmIdCount, greaterThan(0))
        directMessageDao.deleteAllDirectMessages(account)

        queryDatabase("SELECT id FROM direct_messages", null).use { cursor ->
            assertThat("Incorrect number of direct messages", cursor.count, `is`(0))
        }

    }


    @Test
    fun deleteAllDirectMessagesIgnoresOtherAccount() {
        val databaseSize = 3
        val account = 1
        val otherAccount = account + 1

        val dmIdCount = (0 until databaseSize).count {
            val sender = User(null, it.toLong(), "Sender $it", "sender$it", "https://twitter.com/img/$it/img.png", false)
            val recipient = User(null, it.toLong() + 50, "Recipient $it", "recipient$it", "https://twitter.com/img/$it/img.png", false)
            val directMessage = DirectMessage(null, account = account, messageId = it.toLong() + 1)
            insertDirectMessage(sender, recipient, directMessage) != null
        }

        assertThat("At least one direct message must be in the database to run this test properly", dmIdCount, greaterThan(0))
        directMessageDao.deleteAllDirectMessages(otherAccount)

        queryDatabase("SELECT id FROM direct_messages", null).use { cursor ->
            assertThat("Incorrect number of direct messages", cursor.count, `is`(dmIdCount))
        }

    }


    @Test
    fun directMessageRejectsDuplicates() {
        val account = 1
        val messageId = 33L

        val results = insertDirectMessage(
                sender = User(null, 1, "Sender 1", "sender1", "https://twitter.com/img/1/img.png", false),
                recipient = User(null, 50, "Recipient 1", "recipient1", "https://twitter.com/img/50/img.png", false),
                directMessage = DirectMessage(null, account = account, messageId = messageId)
        )

        assertThat("Direct message must be in the database to properly run this test", results, notNullValue())
        val duplicateResults = insertDirectMessage(results!!.second.copy(text = "This is a new message"))

        assertThat("Direct messages does not reject duplicates", duplicateResults, nullValue())
    }

    @Test
    fun directMessageDoesntRejectSameMessageIdWithDifferentAccount() {

        val account = 1
        val messageId = 33L
        val otherAccount = account + 1

        val results = insertDirectMessage(
                sender = User(null, 1, "Sender 1", "sender1", "https://twitter.com/img/1/img.png", false),
                recipient = User(null, 50, "Recipient 1", "recipient1", "https://twitter.com/img/50/img.png", false),
                directMessage = DirectMessage(null, account = account, messageId = messageId)
        )

        assertThat("Direct message must be in the database to properly run this test", results, notNullValue())
        val duplicateResults = insertDirectMessage(results!!.second.copy(id = null, account = otherAccount))

        assertThat("Direct messages rejects non-duplicates (same message id with different accounts)", duplicateResults, notNullValue())
    }


    @Test
    fun trimDatabaseTrimsDatabase() {
        val senderId = 1L
        val recipientId = senderId + 1
        val account = 1

        insertUser(makeUser(senderId, senderId, "Sender"))!!
        insertUser(makeUser(recipientId, recipientId, "Recipient"))!!

        runTrimDatabaseTrimsDatabaseTest("direct_messages", account, toDirectMessageFunction(senderId, recipientId), trimFunction)

    }

    @Test
    fun trimDatabaseDoesntTrimSmallDatabase() {
        val senderId = 1L
        val recipientId = senderId + 1
        val account = 1

        insertUser(makeUser(senderId, senderId, "Sender"))!!
        insertUser(makeUser(recipientId, recipientId, "Recipient"))!!

        runTrimDatabaseDoesntTrimSmallDatabase("direct_messages", account, toDirectMessageFunction(senderId, recipientId), trimFunction)

    }

    @Test
    fun trimDatabaseTrimsInDescendingMessageIdOrder() {
        val databaseSize = 5
        val trimSize = 3
        val account = 1

        val messageIds = (0 until databaseSize).mapNotNull {
            val sender = User(null, it.toLong(), "Sender $it", "sender$it", "https://twitter.com/img/$it/img.png", false)
            val recipient = User(null, it.toLong() + 50, "Recipient $it", "recipient$it", "https://twitter.com/img/$it/img.png", false)
            val directMessage = DirectMessage(null, account = account, messageId = it.toLong() + 1)
            insertDirectMessage(sender, recipient, directMessage)?.let { (_, dm) ->
                dm.messageId
            }
        }

        assertThat("There must be at least one direct message in the database", messageIds, hasSize(greaterThan(0)))
        directMessageDao.trimDatabase(account, trimSize)

        val matchers = messageIds.asSequence()
                .sortedByDescending { it }
                .take(trimSize)
                .map { `is`(it) }
                .toList()

        queryDatabase("SELECT message_id FROM direct_messages ORDER BY message_id DESC", null).use { cursor ->
            assertThat("Problem pointing to the first element of the database", cursor.moveToFirst())
            val databaseMessageIds = List(cursor.count) {
                val id = cursor.getLong(0)
                cursor.moveToNext()
                id
            }

            assertThat("Direct messages were not trimmed based on descending message id", databaseMessageIds, contains(matchers))
        }

    }

    @Test
    fun trimDatabaseIgnoresOtherAccounts() {
        val account = 1
        val senderId = 1L
        val recipientId = senderId + 1L

        insertUser(makeUser(senderId, senderId, "Sender"))!!
        insertUser(makeUser(recipientId, recipientId, "Recipient"))!!

        runTrimDatabaseIgnoresOtherAccounts("direct_messages", account, toDirectMessageFunction(senderId, recipientId), trimFunction)

    }


    @Test
    fun senderRestrictsUserDelete() {
        val recipientId = 2L
        val senderId = 1L

        val recipient = insertUser(makeUser(recipientId, recipientId, "Recipient"))!!
        val toRestrictType = { user : User ->
            ContentValues().apply {
                setDirectMessageContentValues(this, DirectMessage(null, account = 1, senderId = user.id!!, recipientId = recipient.id!!, messageId = 1))
            }
        }

        runRestrictUserDeletionTest("direct_messages", userId = senderId, twitterId = senderId, toRestrictType = toRestrictType)

    }

    @Test
    fun recipientRestrictsUserDelete() {
        val senderId = 1L
        val recipientId = 2L

        val sender = insertUser(makeUser(senderId, senderId, "Sender"))!!
        val toRestrictType = { user : User ->
            ContentValues().apply {
                setDirectMessageContentValues(this, DirectMessage(null, account = 1, senderId = sender.id!!, recipientId = user.id!!, messageId = 1))
            }
        }

        runRestrictUserDeletionTest("direct_messages", userId = recipientId, twitterId = recipientId, toRestrictType = toRestrictType)

    }


    private fun setDirectMessageContentValues(contentValues: ContentValues, directMessage: DirectMessage) {
        with(contentValues) {
            put("id", directMessage.id)
            put("account", directMessage.account)
            put("sender_id", directMessage.senderId)
            put("recipient_id", directMessage.recipientId)
            put("message_id", directMessage.messageId)
            put("time", directMessage.time)
            put("text", directMessage.text)
            put("picture_urls", directMessage.pictureUrls)
            put("urls", directMessage.urls)
            put("gif_url", directMessage.gifUrl)
            put("media_length", directMessage.mediaLength)
        }
    }

    @After
    fun clearTables() {
        clearTestDatabase()
    }


    private fun insertDirectMessage(sender: User, recipient: User, directMessage: DirectMessage) : Pair<Pair<User, User>, DirectMessage>? {

        return insertUser(sender)?.let { s ->
            insertUser(recipient)?.let { r ->
                insertDirectMessage(directMessage.copy(senderId = s.id!!, recipientId = r.id!!))?.let { dm ->
                    s to r to dm
                }
            }
        }
    }

    private fun insertDirectMessage(directMessage: DirectMessage) : DirectMessage? {
        val contentValues = ContentValues()
        setDirectMessageContentValues(contentValues, directMessage)
        val id = insertIntoDatabase("direct_messages", SQLiteDatabase.CONFLICT_IGNORE, contentValues)
        return if (id != -1L) directMessage.copy(id = id) else null
    }










    private fun cursorToDisplayDirectMessage(cursor: Cursor) : DisplayDirectMessage {
        return with(cursor) {
            DisplayDirectMessage(
                    directMessageId = getLong(getColumnIndex("id")),
                    messageId = getLong(getColumnIndex("message_id")),
                    time = getLong(getColumnIndex("time")),
                    text = getString(getColumnIndex("text")),
                    pictureUrls = getString(getColumnIndex("picture_urls")),
                    urls = getString(getColumnIndex("urls")),
                    gifUrl = getString(getColumnIndex("gif_url")),
                    mediaLength = getLong(getColumnIndex("media_length")),
                    sender = User(
                            id = getLong(getColumnIndex("sender_id")),
                            twitterId = getLong(getColumnIndex("sender_twitter_id")),
                            name = getString(getColumnIndex("sender_name")),
                            screenName = getString(getColumnIndex("sender_screen_name")),
                            profilePic = getString(getColumnIndex("sender_profile_pic")),
                            isVerified = getInt(getColumnIndex("sender_is_verified")) == 1
                    ),
                    recipient = User(
                            id = getLong(getColumnIndex("recipient_id")),
                            twitterId = getLong(getColumnIndex("recipient_twitter_id")),
                            name = getString(getColumnIndex("recipient_name")),
                            screenName = getString(getColumnIndex("recipient_screen_name")),
                            profilePic = getString(getColumnIndex("recipient_profile_pic")),
                            isVerified = getInt(getColumnIndex("recipient_is_verified")) == 1
                    )

            )
        }

    }


    companion object {


        @BeforeClass
        @JvmStatic fun initDatabase() {
            DaoTest.initTestDatabase()
        }

        @AfterClass
        @JvmStatic fun closeDatabase() {
            DaoTest.closeTestDatabase()
        }
    }
}
