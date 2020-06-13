package com.klinker.android.twitter_l.data.roomdb

import androidx.room.*
import twitter4j.DirectMessage as TwitterDM


@Entity(tableName = "direct_messages")
data class DirectMessage(
        val text: String,
        @Embedded(prefix = "sender_") val sender: User,
        @Embedded(prefix = "recipient_") val recipient: User,
        val account: Int,
        @PrimaryKey(autoGenerate = true) val id: Long? = null
) {
    constructor(directMessage: TwitterDM, account: Int) : this(
            directMessage.text,
            User(directMessage.sender),
            User(directMessage.recipient),
            account
    )
}

@Dao
interface DirectMessageDao {

    @Insert
    fun insertDirectMessage(directMessage: DirectMessage) : Long?
    
    @Update
    fun updateDirectMessage(directMessage: DirectMessage)

    @Delete
    fun deleteDirectMessage(directMessage: DirectMessage)

    @Query("SELECT * FROM direct_messages WHERE account = :account LIMIT :pageSize OFFSET ((:page - 1) * :pageSize)")
    fun getDirectMessages(account: Int, page: Int = 1, pageSize: Int = 200) : List<DirectMessage>

}