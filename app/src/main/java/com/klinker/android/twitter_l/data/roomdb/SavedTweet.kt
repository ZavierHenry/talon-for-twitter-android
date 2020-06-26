package com.klinker.android.twitter_l.data.roomdb

import androidx.room.*
import twitter4j.Status


@Entity(tableName = "saved_tweets")
data class SavedTweet @JvmOverloads constructor(
        @Embedded val tweet: Tweet,
        val account: Int,
        @PrimaryKey(autoGenerate = true) val id: Long = 0
) {
    constructor(status: Status, account: Int) : this(Tweet(status), account)
}

@Dao
abstract class SavedTweetDao : BaseDao<SavedTweet>() {

    fun insert(vararg entities: SavedTweet): List<SavedTweet> {
        return insertEntities(*entities).mapIndexed { index, id ->
            entities[index].copy(id = id)
        }
    }

    fun insert(entities: List<SavedTweet>): List<SavedTweet> {
        return insertEntities(entities).mapIndexed { index, id ->
            entities[index].copy(id = id)
        }
    }

    fun insert(entity: SavedTweet): SavedTweet {
        val id = insertEntity(entity)
        return entity.copy(id = id)
    }

    @Query("SELECT * FROM saved_tweets WHERE account = :account ORDER BY tweet_id ASC LIMIT :pageSize OFFSET ((:page - 1) * :pageSize)")
    abstract fun getSavedTweets(account: Int, page: Int = 1, pageSize: Int = 200) : List<SavedTweet>
}