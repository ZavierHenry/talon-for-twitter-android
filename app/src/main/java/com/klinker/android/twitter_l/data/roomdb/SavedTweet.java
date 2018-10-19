package com.klinker.android.twitter_l.data.roomdb;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import twitter4j.Status;


@Entity(tableName = "saved_tweets",
        indices = { @Index(value = {"tweet_id", "account"}, unique = true)},
        foreignKeys = { @ForeignKey(entity = Tweet.class, parentColumns = "id", childColumns = "tweet_id", onDelete = ForeignKey.CASCADE)})
public class SavedTweet {

    @PrimaryKey(autoGenerate = true)
    public Long id;

    @ColumnInfo
    public int account;

    @ColumnInfo(name="tweet_id")
    public long tweetId;

    public SavedTweet(long id, int account, long tweetId) {
        this.id = id;
        this.account = account;
        this.tweetId = tweetId;
    }

    public SavedTweet(Status status, int account) {
        this.account = account;
        this.tweetId = status.getId();
    }

}
