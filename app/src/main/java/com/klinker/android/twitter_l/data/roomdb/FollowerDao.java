package com.klinker.android.twitter_l.data.roomdb;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Transaction;

@Dao
public abstract class FollowerDao implements UserDao {

    @Transaction
    public void insertFollower(Follower follower) {
        insertFollowerOnlyData(follower);
    }

    @Insert
    abstract void insertFollowerOnlyData(Follower follower);

    @Delete
    abstract void deleteFollower(Follower follower);



}