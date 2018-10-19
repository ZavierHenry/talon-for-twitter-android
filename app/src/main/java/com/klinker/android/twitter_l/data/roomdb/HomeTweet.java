package com.klinker.android.twitter_l.data.roomdb;


import androidx.media.AudioAttributesCompat;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import twitter4j.Status;


@Entity(tableName = "home_tweets", indices = { @Index(value = {"tweet_id", "account"}, unique = true)})
public class HomeTweet {

    @PrimaryKey(autoGenerate = true)
    public final Long id;

    @ColumnInfo
    public final int account;

    @ColumnInfo(name="is_unread")
    public boolean isUnread;

    @ColumnInfo(name="tweet_id")
    public final long tweetId;

    @ColumnInfo(name="is_current_pos")
    public boolean isCurrentPos;


    public HomeTweet(long id, int account, boolean isUnread, long tweetId, boolean isCurrentPos) { ;
        this.id = id;
        this.account = account;
        this.isUnread = isUnread;
        this.tweetId = tweetId;
        this.isCurrentPos = isCurrentPos;
    }


    public HomeTweet(Status status, int account) {
        this.id = null;
        this.account = account;
        this.tweetId = status.getId();
        this.isUnread = true;
        this.isCurrentPos = false;
    }


}
