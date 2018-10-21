package com.klinker.android.twitter_l.data.roomdb.entities;


import android.renderscript.Script;

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

    @ColumnInfo
    public String text;

    public ScheduledTweet(int alarmId, String text, long time, int account) {
        this.alarmId = alarmId;
        this.text = text;
        this.time = time;
        this.account = account;
    }

}
