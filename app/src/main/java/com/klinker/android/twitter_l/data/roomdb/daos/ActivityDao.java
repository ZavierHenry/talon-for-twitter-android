package com.klinker.android.twitter_l.data.roomdb.daos;


import com.klinker.android.twitter_l.data.roomdb.entities.Activity;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public abstract class ActivityDao {

    @Insert
    abstract void insertActivitySpecificInfo(Activity activity);

    @Query("SELECT * FROM activities WHERE account = :account")
    abstract Activity getAllActivities(int account);

}
