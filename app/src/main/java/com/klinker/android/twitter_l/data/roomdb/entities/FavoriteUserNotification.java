package com.klinker.android.twitter_l.data.roomdb.entities;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorite_user_notifications", indices = { @Index(value = "tweet_id")})
public class FavoriteUserNotification {

    @PrimaryKey(autoGenerate = true)
    public Long id;

    @ColumnInfo(name = "tweet_id")
    public long tweetId;

    @Ignore
    public FavoriteUserNotification(long tweetId) {
        this.id = null;
        this.tweetId = tweetId;
    }

    public FavoriteUserNotification(long id, long tweetId) {
        this.id = id;
        this.tweetId = tweetId;
    }

}
