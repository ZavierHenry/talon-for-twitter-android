package com.klinker.android.twitter_l.data.roomdb.entities;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorite_users",
        indices = {@Index(value = { "user_twitter_id", "account"}, unique = true), @Index(value = "account"), @Index(value = "user_id")},
        foreignKeys = { @ForeignKey( entity = User.class, parentColumns = "id", childColumns = "user_id", onDelete = ForeignKey.CASCADE)})
public class FavoriteUser {

    @PrimaryKey(autoGenerate = true)
    public Long id;

    @ColumnInfo
    public int account;

    @ColumnInfo(name = "user_id")
    public Long userId;

    @ColumnInfo(name = "user_twitter_id")
    public long twitterId;

    public FavoriteUser(twitter4j.User user, int account) {
        this.id = null;
        this.account = account;
        this.userId = null;
        this.twitterId = user.getId();
    }

    public FavoriteUser(long id, int account, long userId, long twitterId) {
        this.id = id;
        this.account = account;
        this.userId = userId;
        this.twitterId = twitterId;
    }

}
