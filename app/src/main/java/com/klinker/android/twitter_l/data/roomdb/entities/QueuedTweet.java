package com.klinker.android.twitter_l.data.roomdb.entities;


import com.google.android.gms.wearable.ChannelClient;

import org.jetbrains.annotations.NotNull;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "queued_tweets", indices = { @Index(value = { "text", "account" })})
public class QueuedTweet {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "queued_tweet_id")
    public final Integer id;

    @ColumnInfo
    public int account;

    @ColumnInfo
    @NotNull
    public String text;

    @Ignore
    public QueuedTweet(@NotNull String text, int account) {
        this.id = null;
        this.text = text;
        this.account = account;
    }

    public QueuedTweet(int id, @NotNull String text, int account) {
        this.id = id;
        this.text = text;
        this.account = account;
    }

}
