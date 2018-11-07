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


}
