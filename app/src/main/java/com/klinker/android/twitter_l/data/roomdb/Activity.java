package com.klinker.android.twitter_l.data.roomdb;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "activities")
public class Activity {

    @PrimaryKey(autoGenerate = true)
    public Long id;

    @ColumnInfo
    public int account;

    @ColumnInfo(name = "tweet_id")
    public Long tweetId;

    @ColumnInfo
    public long time;

    @ColumnInfo
    public String text;

    @ColumnInfo
    public String title;

    @ColumnInfo(name = "user_id")
    public long userId;


}
