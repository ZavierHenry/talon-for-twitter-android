package com.klinker.android.twitter_l.mockentities

import android.content.ContentValues
import com.klinker.android.twitter_l.data.roomdb.DirectMessage
import com.klinker.android.twitter_l.data.roomdb.ListStringConverter
import com.klinker.android.twitter_l.data.roomdb.User
import com.klinker.android.twitter_l.data.sq_lite.DMSQLiteHelper
import com.klinker.android.twitter_l.mockentities.MockUtilities.Companion.makeMockUser

data class MockTransferDirectMessage(override val mockEntity: MockDirectMessage) : MockTransferEntity<MockDirectMessage> {
    private val directMessage = mockEntity.directMessage

    constructor(
            account: Int,
            twitterId: Long = 1L,
            text: String = "",
            time: Long = 0L,
            sender: User = makeMockUser("chrislhayes", "Chris Hayes", "Sender.jpg", 1L),
            recipient: User = makeMockUser("parkermolloy", "Parker Molloy", "Recipient.jpg", 2L),
            id: Long = 0
    ) : this(MockDirectMessage(account, twitterId, text, time, sender, recipient, id))

    override fun copyId(id: Long): MockTransferEntity<MockDirectMessage> {
        return this.copy(mockEntity = mockEntity.copy(directMessage = directMessage.copy(id = id)))
    }

    override fun toSQLiteContentValues(id: Long): ContentValues {
        val converter = ListStringConverter()

        return ContentValues().apply {
            put(DMSQLiteHelper.COLUMN_ACCOUNT, directMessage.account)
            put(DMSQLiteHelper.COLUMN_TEXT, directMessage.text)
            put(DMSQLiteHelper.COLUMN_TWEET_ID, directMessage.twitterId)
            put(DMSQLiteHelper.COLUMN_NAME, directMessage.sender.name)
            put(DMSQLiteHelper.COLUMN_PRO_PIC, directMessage.sender.profilePic)
            put(DMSQLiteHelper.COLUMN_SCREEN_NAME, directMessage.sender.screenName)
            put(DMSQLiteHelper.COLUMN_TIME, directMessage.time)
            put(DMSQLiteHelper.COLUMN_RETWEETER, directMessage.recipient.screenName)
            put(DMSQLiteHelper.COLUMN_EXTRA_ONE, directMessage.recipient.profilePic)
            put(DMSQLiteHelper.COLUMN_EXTRA_TWO, directMessage.recipient.name)
            //put(DMSQLiteHelper.COLUMN_PIC_URL, directMessage.media convert to string)
            //put(DMSQLiteHelper.COLUMN_EXTRA_THREE, directMessage.gif url)
            //put in media

            if (id != -1L) {
                put(DMSQLiteHelper.COLUMN_ID, id)
            }
        }
    }

}