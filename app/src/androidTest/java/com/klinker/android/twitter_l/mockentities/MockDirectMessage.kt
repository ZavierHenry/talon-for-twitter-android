package com.klinker.android.twitter_l.mockentities

import android.content.ContentValues
import android.database.Cursor
import com.klinker.android.twitter_l.mockentities.MockUtilities.Companion.makeMockUser
import com.klinker.android.twitter_l.data.roomdb.DirectMessage
import com.klinker.android.twitter_l.data.roomdb.User

data class MockDirectMessage(val directMessage: DirectMessage) : MockEntity {

    override val id get() = directMessage.id

    constructor(
            account: Int,
            twitterId: Long = 1L,
            text: String = "",
            time: Long = 0L,
            sender: User = makeMockUser("chrislhayes", "Chris Hayes", "Sender.jpg", 23243L),
            recipient: User = makeMockUser("parkermolloy", "Parker Molloy", "Recipient.jpg", 28393L),
            id: Long = 0
    ) : this(DirectMessage(twitterId, text, time, sender, recipient, account, id))

    constructor(cursor: Cursor) : this(
            cursor.getInt(cursor.getColumnIndex("account")),
            cursor.getLong(cursor.getColumnIndex("twitter_id")),
            cursor.getString(cursor.getColumnIndex("text")),
            cursor.getLong(cursor.getColumnIndex("time")),
            MockUtilities.cursorToUser(cursor, "sender_"),
            MockUtilities.cursorToUser(cursor, "recipient_"),
            cursor.getLong(cursor.getColumnIndex("id"))
    )

    override fun toContentValues(): ContentValues {
        val sender = MockUtilities.userToContentValues(directMessage.sender, "sender_")
        val recipient = MockUtilities.userToContentValues(directMessage.recipient, "recipient_")
        return ContentValues().apply {
            putAll(sender)
            putAll(recipient)
            put("twitter_id", directMessage.twitterId)
            put("text", directMessage.text)
            put("time", directMessage.time)
            put("account", directMessage.account)
        }
    }

}