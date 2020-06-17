package com.klinker.android.twitter_l

import android.content.ContentValues
import android.database.Cursor
import com.klinker.android.twitter_l.MockUtilities.Companion.makeMockUser
import com.klinker.android.twitter_l.data.roomdb.DirectMessage
import com.klinker.android.twitter_l.data.roomdb.User

data class MockDirectMessage(val directMessage: DirectMessage) : MockEntity {

    constructor(
            account: Int,
            text: String = "",
            time: Long = 0L,
            sender: User = makeMockUser("chrislhayes", "Chris Hayes", "Sender.jpg", 23243L),
            recipient: User = makeMockUser("parkermolloy", "Parker Molloy", "Recipient.jpg", 28393L),
            id: Long? = null
    ) : this(DirectMessage(text, time, sender, recipient, account, id))

    constructor(cursor: Cursor) : this(
            cursor.getInt(cursor.getColumnIndex("account")),
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
            put("text", directMessage.text)
            put("time", directMessage.time)
            put("account", directMessage.account)
        }
    }

}