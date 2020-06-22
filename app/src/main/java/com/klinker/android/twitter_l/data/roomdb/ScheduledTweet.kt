package com.klinker.android.twitter_l.data.roomdb

import androidx.room.*


@Entity(tableName = "scheduled_tweets", indices = [Index(value = ["alarm_id"], unique = true)])
data class ScheduledTweet(
        val text: String,
        val time: Long,
        @ColumnInfo(name = "alarm_id") val alarmId: Long,
        val account: Int,
        @PrimaryKey(autoGenerate = true) var id: Long? = null
)


@Dao
abstract class ScheduledTweetDao : BaseDao<ScheduledTweet>() {

    @Query("SELECT * FROM scheduled_tweets WHERE account = :account ORDER BY time DESC LIMIT :pageSize OFFSET ((:page - 1) * :pageSize)")
    abstract fun getScheduledTweets(account: Int, page: Int = 1, pageSize: Int = 200) : List<ScheduledTweet>

    @Query("SELECT MIN(time) FROM scheduled_tweets WHERE time > :currentTime")
    abstract fun getNextScheduledTime(currentTime: Long) : Long?

    @Query("SELECT * FROM scheduled_tweets WHERE time > :currentTime ORDER BY time DESC LIMIT 1")
    abstract fun getNextScheduledTweet(currentTime: Long) : ScheduledTweet?

}