package com.klinker.android.twitter_l.data.roomdb.daos;


import com.klinker.android.twitter_l.data.roomdb.entities.FavoriteTweet;

import androidx.room.Dao;
import androidx.room.Insert;

@Dao
public abstract class FavoriteTweetDao {

    @Insert
    abstract void insertFavoriteTweetSpecificData(FavoriteTweet favoriteTweet);




}
