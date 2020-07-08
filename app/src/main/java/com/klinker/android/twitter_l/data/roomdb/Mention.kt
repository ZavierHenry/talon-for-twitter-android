package com.klinker.android.twitter_l.data.roomdb

import android.content.SharedPreferences
import androidx.room.*
import twitter4j.Status
import java.util.*


@Entity(tableName = "mentions", indices = [Index(value = ["tweet_id", "account"], unique = true)])
data class Mention @JvmOverloads constructor(
        @Embedded val tweet: Tweet,
        val account: Int,
        @ColumnInfo(name = "is_unread") val isUnread: Boolean,
        @ColumnInfo(name = "is_muted") val isMuted: Boolean = false,
        @PrimaryKey(autoGenerate = true) val id: Long = 0
) : BaseDao.TalonEntity<Mention> {

    private constructor(tweet: Tweet, account: Int, isUnread: Boolean, users: List<String>, hashtags: List<String>, expressions: List<String>) :
            this(
                    tweet,
                    account,
                    isUnread,
                    users.contains(tweet.author.screenName.toLowerCase(Locale.getDefault()))
                            || tweet.hashtags.any { h1 -> hashtags.any { h2 -> h1.contains(h2, ignoreCase = true)}}
                            || expressions.any { tweet.text.contains(it, ignoreCase = true) }
            )

    constructor(status: Status, account: Int, isUnread: Boolean) : this(Tweet(status), account, isUnread)

    constructor(status: Status, account: Int, isUnread: Boolean, sharedPreferences: SharedPreferences) :
            this(
                    Tweet(status),
                    account,
                    isUnread,
                    sharedPreferences.getString("muted_users", "")
                            ?.split(" ")
                            ?.map { it.toLowerCase(Locale.getDefault()) } ?: emptyList(),
                    sharedPreferences.getString("muted_hashtags", "")
                            ?.split(" ")
                            ?.map { it.toLowerCase(Locale.getDefault()) } ?: emptyList(),
                    sharedPreferences.getString("muted_regex", "")
                            ?.split("   ")
                            ?.map { it.toLowerCase(Locale.getDefault()) } ?: emptyList()
            )


    override fun copyWithId(id: Long): Mention {
        return this.copy(id = id)
    }

}

@Dao
abstract class MentionDao : BaseDao<Mention>() {

    @Query("SELECT * FROM mentions WHERE account = :account ORDER BY tweet_id DESC LIMIT :pageSize OFFSET ((:page - 1) * :pageSize)")
    abstract fun getUnmutedMentions(account: Int, page: Int = 1, pageSize: Int = 200) : List<Mention>

    @Query("SELECT * FROM mentions WHERE account = :account AND is_unread = 1 ORDER BY tweet_id DESC LIMIT :pageSize OFFSET ((:page - 1) * :pageSize)")
    abstract fun getUnmutedUnreadMentions(account: Int, page: Int = 1, pageSize: Int = 200) : List<Mention>

    @Query("SELECT * FROM mentions WHERE account = :account AND is_muted = 0 ORDER BY tweet_id DESC LIMIT :pageSize OFFSET ((:page - 1) * :pageSize)")
    abstract fun getMentionsWithoutMutedMentions(account: Int, page: Int = 1, pageSize: Int = 200) : List<Mention>

    @Query("SELECT * FROM mentions WHERE account = :account AND is_unread = 1 AND is_muted = 0 ORDER BY tweet_id DESC LIMIT :pageSize OFFSET ((:page - 1) * :pageSize)")
    abstract fun getUnreadMentionsWithoutMutedMentions(account: Int, page: Int = 1, pageSize: Int = 200) : List<Mention>

    @Query("UPDATE mentions SET is_muted = :isMuted WHERE account = :account AND user_screen_name LIKE :mention")
    abstract fun setMutedByMention(account: Int, mention: String, isMuted: Boolean)

    @Query("UPDATE mentions SET is_muted = :isMuted WHERE account = :account AND hashtags LIKE :hashtag")
    abstract fun setMutedByHashtagPattern(account: Int, hashtag: String, isMuted: Boolean)

    @Ignore
    fun setMutedByHashtag(account: Int, hashtag: String, isMuted: Boolean) {
        setMutedByHashtagPattern(account, "%${hashtag}%", isMuted)
    }

    @Query("UPDATE mentions SET is_muted = :isMuted WHERE account = :account AND text LIKE :expression")
    abstract fun setMutedByExpressionPattern(account: Int, expression: String, isMuted: Boolean)

    @Ignore
    fun setMutedByExpression(account: Int, expression: String, isMuted: Boolean) {
        setMutedByExpressionPattern(account, "%${expression.replace("'", "''")}%", isMuted)
    }


    @Query("DELETE FROM mentions WHERE account = :account AND id NOT IN (SELECT id FROM mentions WHERE account = :account ORDER BY id DESC LIMIT :size)")
    abstract fun trimTable(account: Int, size: Int)

    @Query("DELETE FROM mentions WHERE tweet_id = :tweetId")
    abstract fun deleteTweet(tweetId: Long)

    @Query("DELETE FROM mentions WHERE account = :account")
    abstract fun deleteAllTweets(account: Int)

    @Query("SELECT COUNT(1) FROM mentions WHERE account = :account AND is_unread = 1 AND is_muted = 0")
    abstract fun getUnreadCount(account: Int) : Int

    @Query("UPDATE mentions SET is_unread = 0 WHERE tweet_id = :tweetId AND is_unread = 1")
    abstract fun markAsRead(tweetId: Long)

    @Query("UPDATE mentions SET is_unread = 0 WHERE account = :account AND is_unread = 1")
    abstract fun markAllRead(account: Int)

    @Ignore
    fun create(status: Status, account: Int, isUnread: Boolean, sharedPreferences: SharedPreferences) : Mention {
        return this.insert(Mention(status, account, isUnread, sharedPreferences))
    }

    @Ignore
    fun create(statuses: List<Status>, account: Int, isUnread: Boolean, sharedPreferences: SharedPreferences) : List<Mention> {
        return this.insert(statuses.map { status -> Mention(status, account, isUnread, sharedPreferences) })
    }

}