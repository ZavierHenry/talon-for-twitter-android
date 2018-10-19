package com.klinker.android.twitter_l.data.roomdb.entities;


import java.util.ArrayList;
import java.util.List;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "followers", indices = { @Index(value = {"user_id", "account"}, unique = true)})
public class Follower {

    @PrimaryKey(autoGenerate = true)
    public Long id;

    @ColumnInfo
    public int account;

    @ColumnInfo(name = "user_id")
    public long userId;

    public Follower(long id, int account, long userId) {

        this.id = id;
        this.account = account;
        this.userId = userId;

    }

    @Ignore
    public Follower(int account, long userId) {
        this.id = null;
        this.account = account;
        this.userId = userId;
    }


    public Follower(twitter4j.User user, int account) {
        this.id = null;
        this.account = account;
        this.userId = user.getId();
    }


}
