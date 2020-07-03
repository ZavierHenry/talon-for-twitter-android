package com.klinker.android.twitter_l.data.roomdb

import androidx.room.*
import twitter4j.Status


@Entity(tableName = "mentions", indices = [Index(value = ["tweet_id", "account"], unique = true)])
data class Mention @JvmOverloads constructor(
        @Embedded val tweet: Tweet,
        val account: Int,
        @ColumnInfo(name = "is_unread") val isUnread: Boolean,
        @PrimaryKey(autoGenerate = true) val id: Long = 0
) : BaseDao.TalonEntity<Mention> {
    constructor(status: Status, account: Int, isUnread: Boolean) : this(Tweet(status), account, isUnread)

    override fun copyWithId(id: Long): Mention {
        return this.copy(id = id)
    }

}

@Dao
abstract class MentionDao : BaseDao<Mention>() {

    @Query("SELECT * FROM mentions WHERE account = :account ORDER BY tweet_id ASC LIMIT :pageSize OFFSET ((:page - 1) * :pageSize)")
    abstract fun getMentions(account: Int, page: Int = 1, pageSize: Int = 200) : List<Mention>

    @Query("SELECT * FROM mentions WHERE account = :account AND is_unread ORDER BY tweet_id ASC LIMIT :pageSize OFFSET ((:page - 1) * :pageSize)")
    abstract fun getUnreadMentions(account: Int, page: Int = 1, pageSize: Int = 200) : List<Mention>

    @Query("DELETE FROM mentions WHERE account = :account AND id NOT IN (SELECT id FROM mentions WHERE account = :account ORDER BY id DESC LIMIT :size)")
    abstract fun trimDatabase(account: Int, size: Int)

    @Ignore
    fun create(status: Status, account: Int, isUnread: Boolean) : Mention {
        return this.insert(Mention(status, account, isUnread))
    }

}