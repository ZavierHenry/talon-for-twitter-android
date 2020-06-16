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
abstract class ScheduledTweetDao {

    @Insert
    abstract fun insertScheduledTweet(scheduledTweet: ScheduledTweet) : Long?

    @Update
    abstract fun updateScheduledTweet(scheduledTweet: ScheduledTweet)

    @Delete
    abstract fun deleteScheduledTweet(scheduledTweet: ScheduledTweet)

    @Query("SELECT * FROM scheduled_tweets WHERE account = :account ORDER BY time DESC LIMIT :pageSize OFFSET ((:page - 1) * :pageSize)")
    abstract fun getScheduledTweets(account: Int, page: Int = 1, pageSize: Int = 200) : List<ScheduledTweet>

}