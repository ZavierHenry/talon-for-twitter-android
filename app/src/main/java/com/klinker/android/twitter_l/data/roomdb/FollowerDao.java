package com.klinker.android.twitter_l.data.roomdb;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

@Dao
public abstract class FollowerDao {

    @Transaction
    public void insertFollower(Follower follower) {
        insertFollowerOnlyData(follower);
    }

    @Insert
    abstract void insertFollowerOnlyData(Follower follower);

    @Delete
    abstract void deleteFollower(Follower follower);

    @Query("DELETE FROM followers WHERE user_id = :userId AND account = :account")
    abstract void deleteFollower(long userId, int account);

    @Query("DELETE FROM followers WHERE account = :account")
    abstract void deleteAllFollowers(int account);


}