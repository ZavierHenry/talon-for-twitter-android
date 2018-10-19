package com.klinker.android.twitter_l.data.roomdb.entities;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "scheduled_tweets")
public class ScheduledTweet {

    @PrimaryKey
    @ColumnInfo(name = "alarm_id")
    public int alarmId;

    @ColumnInfo
    public long time;

    @ColumnInfo
    public int account;

}
