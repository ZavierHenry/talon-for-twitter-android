package com.klinker.android.twitter_l.data.roomdb;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import twitter4j.Status;

@Entity(tableName = "favorite_tweets")
public class FavoriteTweet {

    @PrimaryKey
    public Long id;

    @ColumnInfo
    public int account;

    @ColumnInfo(name = "is_unread")
    public boolean isUnread;

    @ColumnInfo(name = "tweet_id")
    public long tweetId;

    public FavoriteTweet(long id, int account, boolean isUnread, long tweetId) {
        this.id = id;
        this.account = account;
        this.isUnread = isUnread;
        this.tweetId = tweetId;
    }

    public FavoriteTweet(Status status, int account) {

        this.id = null;
        this.account = account;
        this.isUnread = true;
        this.tweetId = status.getId();
    }

}
