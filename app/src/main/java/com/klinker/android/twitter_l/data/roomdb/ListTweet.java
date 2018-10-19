package com.klinker.android.twitter_l.data.roomdb;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import twitter4j.Status;

@Entity(tableName = "list_tweets",
        indices = { @Index(value = { "list_id", "tweet_id" }, unique = true)},
        foreignKeys = { @ForeignKey(entity = Tweet.class, parentColumns = "id", childColumns = "tweet_id", onDelete = ForeignKey.CASCADE)})
public class ListTweet {


    @PrimaryKey(autoGenerate = true)
    public Long id;

    @ColumnInfo(name = "tweet_id")
    public long tweetId;

    @ColumnInfo(name = "list_id")
    public long listId;

    public ListTweet(long id, long tweetId, long listId) {
        this.id = id;
        this.tweetId = tweetId;
        this.listId = listId;
    }

    public ListTweet(Status status, long listId) {
        this.tweetId = status.getId();
        this.listId = listId;
    }


}
