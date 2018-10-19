package com.klinker.android.twitter_l.data.roomdb.daos;


import com.klinker.android.twitter_l.data.roomdb.entities.Mention;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


@Dao
public abstract class MentionDao extends TweetDao {

    @Insert
    abstract void insertMention(Mention mention);


    @Query("SELECT tweet_id FROM mentions WHERE account = :account ORDER BY tweet_id DESC LIMIT 2")
    abstract long[] getLastestTwoIds(int account);


    @Query("DELETE FROM mentions WHERE account = :account AND id < (SELECT MIN(id) from mentions WHERE account = :account ORDER BY id DESC LIMIT :trimSize)")
    abstract void trimDatabase(int account, int trimSize);

    @Update
    abstract void updateMention(Mention mention);

    @Query("UPDATE mentions SET is_unread = 0 WHERE tweet_id = :tweetId AND account = :account")
    abstract void markRead(long tweetId, int account);


}
