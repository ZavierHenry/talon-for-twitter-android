package com.klinker.android.twitter_l.data.roomdb

import androidx.room.*
import twitter4j.Status


@Entity(tableName = "user_tweets", indices = [Index(value = ["tweet_id", "user_user_id"], unique = true)])
data class UserTweet @JvmOverloads constructor(
        @Embedded val tweet: Tweet,
        val account: Int,
        @PrimaryKey(autoGenerate = true) val id: Long = 0
) {
    constructor(status: Status, account: Int) : this(Tweet(status), account)
}

@Dao
abstract class UserTweetDao : BaseDao<UserTweet>() {

    fun insert(entity: UserTweet): UserTweet {
        val id = insertEntity(entity)
        return entity.copy(id = id)
    }

    fun insert(vararg entities: UserTweet): List<UserTweet> {
        return insertEntities(*entities).mapIndexed { index, id ->
            entities[index].copy(id = id)
        }
    }

    fun insert(entities: List<UserTweet>): List<UserTweet> {
        return insertEntities(entities).mapIndexed { index, id ->
            entities[index].copy(id = id)
        }
    }

    @Query("SELECT * FROM user_tweets WHERE account = :account ORDER BY tweet_id ASC LIMIT :pageSize OFFSET ((:page - 1) * :pageSize)")
    abstract fun getUserTweets(account: Int, page: Int = 1, pageSize: Int = 250) : List<UserTweet>

    @Query("DELETE FROM user_tweets WHERE user_user_id = :userId and id NOT IN (SELECT id FROM user_tweets WHERE user_user_id = :userId ORDER BY id DESC LIMIT :size)")
    abstract fun trimDatabase(userId: Long, size: Int)

}