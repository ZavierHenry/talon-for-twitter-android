package com.klinker.android.twitter_l.data.roomdb.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import twitter4j.Status;


@Entity(tableName = "user_tweets", indices = { @Index(value = {"user_id", "tweet_id"}, unique = true)})
public class UserTweet {

    @PrimaryKey(autoGenerate = true)
    public Long id;

    @ColumnInfo(name = "user_id")
    public long userId;

    @ColumnInfo(name = "tweet_id")
    public long tweetId;


    public UserTweet(long id, long userId, long tweetId) {
        this.id = id;
        this.userId = userId;
        this.tweetId = tweetId;
    }

    public UserTweet(Status status) {

        this.id = null;
        this.tweetId = status.getId();

        if (status.isRetweet()) {
            this.userId = status.getRetweetedStatus().getUser().getId();
        } else {
            this.userId = status.getUser().getId();
        }

    }


}
