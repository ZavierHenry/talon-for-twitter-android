package com.klinker.android.twitter_l.data.roomdb

import androidx.room.*
import twitter4j.DirectMessage as TwitterDM

@Entity(tableName = "direct_messages", indices = [Index(value = ["twitter_id", "account"], unique = true)])
data class DirectMessage @JvmOverloads constructor(
        @ColumnInfo(name = "twitter_id") val twitterId: Long,
        val text: String,
        val time: Long,
        @Embedded(prefix = "sender_") val sender: User,
        @Embedded(prefix = "recipient_") val recipient: User,
        val account: Int,
        @PrimaryKey(autoGenerate = true) val id: Long = 0
) : BaseDao.TalonEntity<DirectMessage> {

    override fun copyWithId(id: Long): DirectMessage {
        return this.copy(id = id)
    }

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
abstract class DirectMessageDao : BaseDao<DirectMessage>() {

    @Query("SELECT * FROM direct_messages WHERE account = :account LIMIT :pageSize OFFSET ((:page - 1) * :pageSize)")
    abstract fun getDirectMessages(account: Int, page: Int = 1, pageSize: Int = 200) : List<DirectMessage>

    @Query("DELETE FROM direct_messages WHERE account = :account AND id NOT IN (SELECT id FROM direct_messages WHERE account = :account ORDER BY id DESC LIMIT :size)")
    abstract fun trimDatabase(account: Int, size: Int)

    @Ignore
    fun create(directMessage: TwitterDM, account: Int) : DirectMessage {
        return this.insert(DirectMessage(directMessage, account))
    }

}