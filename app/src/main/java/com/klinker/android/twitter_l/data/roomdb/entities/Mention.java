package com.klinker.android.twitter_l.data.roomdb.entities;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import twitter4j.Status;


@Entity(tableName = "mentions",
        indices = { @Index(value = {"account", "tweet_id"}, unique = true) },
        foreignKeys = { @ForeignKey(
                entity = Tweet.class,
                parentColumns = "id",
                childColumns = "tweet_id")})
public class Mention {

    @PrimaryKey(autoGenerate = true)
    public Long id;

    @ColumnInfo(name="account")
    public int account;

    @ColumnInfo(name="tweet_id")
    public long tweetId;


    @ColumnInfo(name="is_unread", typeAffinity = ColumnInfo.INTEGER)
    public boolean isUnread;


    public Mention(long id, int account, long tweetId, boolean isUnread) {
        this.id = id;
        this.account = account;
        this.tweetId = tweetId;
        this.isUnread = isUnread;
    }


    public Mention(Status status, int account) {
        this.id = null;
        this.account = account;
        this.tweetId = status.getId();
        this.isUnread = true;
    }


}





