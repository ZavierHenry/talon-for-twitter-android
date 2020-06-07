package com.klinker.android.twitter_l.data.roomdb

import androidx.room.*
import twitter4j.Status


@Entity(tableName = "mentions")
data class Mention(
        @Embedded val tweet: Tweet,
        val account: Int,
        @ColumnInfo(name = "is_unread") val isUnread: Boolean,
        @PrimaryKey val id: Long? = null
) {
    constructor(status: Status, account: Int, is_unread: Boolean) : this(Tweet(status), account, is_unread)
}

@Dao
interface MentionDao {

    @Insert
    fun insertMention(mention: Mention) : Long?

    @Delete
    fun deleteMention(mention: Mention)

    @Query("SELECT * FROM mentions WHERE account = :account ORDER BY tweet_id ASC LIMIT :pageSize OFFSET ((:page - 1) * :pageSize)")
    fun getMentions(account: Int, page: Int = 1, pageSize: Int = 200)

    @Query("SELECT * FROM mentions WHERE account = :account AND is_unread ORDER BY tweet_id ASC LIMIT :pageSize OFFSET ((:page - 1) * :pageSize)")
    fun getUnreadMentions(account: Int, page: Int = 1, pageSize: Int = 200)

}