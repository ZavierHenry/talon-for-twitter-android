package com.klinker.android.twitter_l.data.roomdb.daos


import com.klinker.android.twitter_l.data.roomdb.entities.Activity

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
abstract class ActivityDao {





    //abstract fun getDisplayActivities(account: Int, page : Int = 1, pageSize: Int = 60);

    @Query("SELECT id FROM activities WHERE account = :account ORDER BY time DESC LIMIT :size")
    abstract fun getLatestIds(account: Int, size : Int = 2) : List<Long>

    @Query("DELETE FROM activities WHERE account = :account")
    abstract fun deleteAllActivities(account: Int)

    @Query("DELETE FROM activities WHERE id NOT IN(SELECT id FROM activities WHERE account = :account ORDER BY time DESC LIMIT :trimSize)")
    abstract fun trimDatabase(account: Int, trimSize: Int)

}
