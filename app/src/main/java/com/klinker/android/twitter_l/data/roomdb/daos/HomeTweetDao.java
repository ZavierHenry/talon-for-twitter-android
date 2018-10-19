package com.klinker.android.twitter_l.data.roomdb.daos;


import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

@Dao
public abstract class HomeTweetDao extends TweetDao {


    @Transaction
    public void markCurrentPosition(int account, long id) {
        removeCurrentPositionOtherTweets(account, id);
        saveCurrentPosition(account, id);
    }

    @Query("UPDATE home_tweets SET is_current_pos = 1 WHERE account = :account AND id = :id")
    abstract void saveCurrentPosition(int account, long id);


    @Query("UPDATE home_tweets SET is_current_pos = 0 WHERE account = :account AND id <> :markedId")
    abstract void removeCurrentPositionOtherTweets(int account, long markedId);


    @Query("DELETE FROM home_tweets WHERE account = :account AND id < (SELECT MIN(id) FROM home_tweets WHERE account = :account ORDER BY id DESC LIMIT :trimSize)")
    abstract void trimDatabase(int account, int trimSize);



}
