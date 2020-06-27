package com.klinker.android.twitter_l.data.roomdb

import androidx.room.*
import twitter4j.Status


@Entity(tableName = "home_tweets", indices = [Index(value = ["tweet_id", "account"], unique = true)])
data class HomeTweet @JvmOverloads constructor(
        @Embedded val tweet: Tweet,
        val account: Int,
        @ColumnInfo(name = "is_unread") val isUnread: Boolean,
        @ColumnInfo(name = "is_current_position") val isCurrentPosition: Boolean = false,
        @PrimaryKey(autoGenerate = true) val id: Long = 0
) {
    @JvmOverloads constructor(status: Status, account: Int, isUnread: Boolean, isCurrentPosition: Boolean = false) : this(Tweet(status), account, isUnread, isCurrentPosition)
}


@Dao
abstract class HomeTweetDao : BaseDao<HomeTweet>() {

    fun insert(vararg entities: HomeTweet): List<HomeTweet> {
        return insertEntities(*entities).mapIndexed { index, id ->
            entities[index].copy(id = id)
        }
    }

    fun insert(entities: List<HomeTweet>): List<HomeTweet> {
        return insertEntities(entities).mapIndexed { index, id ->
            entities[index].copy(id = id)
        }
    }

    fun insert(entity: HomeTweet): HomeTweet {
        val id = insertEntity(entity)
        return entity.copy(id = id)
    }

    @Query("SELECT * FROM home_tweets WHERE account = :account ORDER BY tweet_id DESC LIMIT :pageSize OFFSET ((:page - 1) * :pageSize)")
    abstract fun getHomeTweets (account: Int, page: Int = 1, pageSize: Int = 200) : List<HomeTweet>

    @Query("DELETE FROM home_tweets WHERE account = :account AND id NOT IN (SELECT id FROM home_tweets WHERE account = :account ORDER BY id DESC LIMIT :size)")
    abstract fun trimDatabase(account: Int, size: Int)

}