package com.klinker.android.twitter_l.data.roomdb.daos


import com.klinker.android.twitter_l.data.roomdb.entities.DirectMessage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

@Dao
abstract class DirectMessageDao {


    @Insert
    internal abstract fun insertDirectMessageSpecificInfo(directMessage: DirectMessage)

    @Query("SELECT * FROM direct_messages WHERE account = :account")
    internal abstract fun getDirectMessages(account: Int): List<DirectMessage>


    @Query("UPDATE direct_messages SET sender_id = :newSenderId WHERE sender_id = :oldSenderId")
    internal abstract fun changeDirectMessageSenderId(oldSenderId: Long, newSenderId: Long)


    @Query("UPDATE direct_messages SET recipient_id = :newRecipientId WHERE recipient_id = :oldRecipientId")
    internal abstract fun changeDirectMessageRecipientId(oldRecipientId: Long, newRecipientId: Long)

    @Transaction
    open fun changeDirectMessageIds(oldId: Long, newId: Long) {
        changeDirectMessageSenderId(oldId, newId)
        changeDirectMessageRecipientId(oldId, newId)
    }

    @Query("DELETE FROM direct_messages WHERE message_id = :messageId")
    abstract fun deleteDirectMessage(messageId: Long)

    @Query("DELETE FROM direct_messages WHERE account = :account")
    abstract fun deleteAllDirectMessages(account: Int)

    @Query("SELECT screen_name FROM direct_messages JOIN users ON sender_id = users.id AND account = :account AND screen_name <> :currentScreenName ORDER BY message_id DESC LIMIT 1")
    abstract fun getNewestScreenName(account: Int, currentScreenName: String) : String?

    @Query("SELECT text FROM direct_messages JOIN users ON sender_id = users.id AND account = :account AND screen_name <> :currentAccountName ORDER BY message_id DESC LIMIT 1")
    abstract fun getNewestMessageText(account: Int, currentAccountName: String) : String?

    //remove html

    @Query("DELETE FROM direct_messages WHERE id IN(SELECT id FROM direct_messages WHERE account = :account ORDER BY id DESC LIMIT -1 OFFSET :trimSize)")
    abstract fun trimDatabase(account: Int, trimSize: Int)


}
