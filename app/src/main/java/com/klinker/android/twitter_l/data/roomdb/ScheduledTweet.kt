package com.klinker.android.twitter_l.data.roomdb

import androidx.room.*

@Entity(tableName = "scheduled_tweets", indices = [Index(value = ["alarm_id"], unique = true)])
data class ScheduledTweet(
        val text: String,
        val time: Long,
        @ColumnInfo(name = "alarm_id") val alarmId: Long,
        val account: Int,
        @PrimaryKey(autoGenerate = true) val id: Long = 0
)

@Dao
abstract class ScheduledTweetDao : BaseDao<ScheduledTweet>() {

    fun insert(entity: ScheduledTweet): ScheduledTweet {
        val id = insertEntity(entity)
        return entity.copy(id = id)
    }

    fun insert(entities: List<ScheduledTweet>): List<ScheduledTweet> {
        return insertEntities(entities).mapIndexed { index, id ->
            entities[index].copy(id = id)
        }
    }

    fun insert(vararg entities: ScheduledTweet): List<ScheduledTweet> {
        return insertEntities(*entities).mapIndexed { index, id ->
            entities[index].copy(id = id)
        }
    }

    @Query("SELECT * FROM scheduled_tweets WHERE account = :account ORDER BY time DESC LIMIT :pageSize OFFSET ((:page - 1) * :pageSize)")
    abstract fun getScheduledTweets(account: Int, page: Int = 1, pageSize: Int = 200) : List<ScheduledTweet>

    @Query("SELECT MIN(time) FROM scheduled_tweets WHERE time > :currentTime")
    abstract fun getNextScheduledTime(currentTime: Long) : Long?

    @Query("SELECT * FROM scheduled_tweets WHERE time > :currentTime ORDER BY time DESC LIMIT 1")
    abstract fun getNextScheduledTweet(currentTime: Long) : ScheduledTweet?

}