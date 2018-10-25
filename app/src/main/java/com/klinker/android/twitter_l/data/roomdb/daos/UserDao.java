package com.klinker.android.twitter_l.data.roomdb.daos;


import com.klinker.android.twitter_l.data.roomdb.entities.User;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

@Dao
public abstract class UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void insertUserSpecificInfo(User user);


    @Delete
    abstract void deleteUser(User user);

    @Query("DELETE from users WHERE id = :id")
    abstract void deleteUser(long id);


    @Query("SELECT id FROM users WHERE screen_name = :screenName")
    abstract long findUserByScreenName(String screenName);





}
