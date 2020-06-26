package com.klinker.android.twitter_l.data.roomdb

import androidx.room.*
import twitter4j.Status


@Entity(tableName = "list_tweets", indices = [Index(value = ["tweet_id", "list_id"], unique = true)])
data class ListTweet @JvmOverloads constructor(
        @Embedded val tweet: Tweet,
        @ColumnInfo(name = "list_id") val listId: Long,
        val account: Int,
        @PrimaryKey(autoGenerate = true) val id: Long = 0
) {
    constructor(status: Status, listId: Long, account: Int) : this(Tweet(status), listId, account)
}

@Dao
abstract class ListTweetDao : BaseDao<ListTweet>() {

    fun insert(entities: List<ListTweet>): List<ListTweet> {
        return insertEntities(entities).mapIndexed { index, id ->
            entities[index].copy(id = id)
        }
    }

    fun insert(vararg entities: ListTweet): List<ListTweet> {
        return insertEntities(*entities).mapIndexed { index, id ->
            entities[index].copy(id = id)
        }
    }

    fun insert(entity: ListTweet): ListTweet {
        val id = insertEntity(entity)
        return entity.copy(id = id)
    }

    @Query("SELECT * FROM list_tweets WHERE account = :account AND list_id = :listId ORDER BY tweet_id ASC LIMIT :pageSize OFFSET ((:page - 1) * :pageSize)")
    abstract fun getListTweets(account: Int, listId: Long, page: Int = 1, pageSize: Int = 200) : List<ListTweet>

    @Query("DELETE FROM list_tweets WHERE list_id = :listId AND id NOT IN (SELECT id FROM list_tweets WHERE list_id = :listId ORDER BY id DESC LIMIT :size)")
    abstract fun trimDatabase(listId: Long, size: Int)
}