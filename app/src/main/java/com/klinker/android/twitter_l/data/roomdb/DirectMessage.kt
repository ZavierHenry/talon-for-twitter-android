package com.klinker.android.twitter_l.data.roomdb

import androidx.room.*
import twitter4j.DirectMessage as TwitterDM


@Entity(tableName = "direct_messages", indices = [Index(value = ["twitter_id", "account"], unique = true)])
data class DirectMessage(
        @ColumnInfo(name = "twitter_id") val twitterId: Long,
        val text: String,
        val time: Long,
        @Embedded(prefix = "sender_") val sender: User,
        @Embedded(prefix = "recipient_") val recipient: User,
        val account: Int,
        @PrimaryKey(autoGenerate = true) val id: Long? = null
) {
    constructor(directMessage: TwitterDM, account: Int) : this(
            directMessage.id,
            directMessage.text,
            directMessage.createdAt.time,
            User(directMessage.sender),
            User(directMessage.recipient),
            account
    )
}

@Dao
abstract class DirectMessageDao {

    @Insert
    abstract fun insertDirectMessage(directMessage: DirectMessage) : Long?
    
    @Update
    abstract fun updateDirectMessage(directMessage: DirectMessage)

    @Delete
    abstract fun deleteDirectMessage(directMessage: DirectMessage)

    @Query("SELECT * FROM direct_messages WHERE account = :account LIMIT :pageSize OFFSET ((:page - 1) * :pageSize)")
    abstract fun getDirectMessages(account: Int, page: Int = 1, pageSize: Int = 200) : List<DirectMessage>

    @Query("DELETE FROM direct_messages WHERE account = :account AND id NOT IN (SELECT id FROM direct_messages WHERE account = :account ORDER BY id DESC LIMIT :size)")
    abstract fun trimDatabase(account: Int, size: Int)

}