package com.klinker.android.twitter_l.data.roomdb;


import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Transaction;

@Dao
public abstract class TweetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void insertTweetSpecificData(Tweet tweet);



    protected List<Tweet> filterMutes(List<Tweet> tweets, List<String> mutedHashtags, List<String> mutedExpressions) {
        return null;
    }


}
