package com.klinker.android.twitter_l.data.roomdb;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

@Dao
public abstract class FavoriteTweetDao {

    @Insert
    abstract void insertFavoriteTweetSpecificData(FavoriteTweet favoriteTweet);




}
