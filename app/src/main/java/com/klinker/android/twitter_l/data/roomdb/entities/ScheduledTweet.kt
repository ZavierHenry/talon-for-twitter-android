package com.klinker.android.twitter_l.data.roomdb.entities


import android.renderscript.Script

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


//TODO: decide whether to put an index on date (may not be worth it)
@Entity(tableName = "scheduled_tweets")
class ScheduledTweet(@field:PrimaryKey
                     @field:ColumnInfo(name = "alarm_id")
                     var alarmId: Long, @field:ColumnInfo
                     var text: String, @field:ColumnInfo
                     var time: Long, @field:ColumnInfo
                     var account: Int)
