package com.klinker.android.twitter_l.data.roomdb.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import com.klinker.android.twitter_l.data.roomdb.entities.UserInteraction
import com.klinker.android.twitter_l.data.roomdb.pojos.DisplayUser


@Dao
abstract class UserInteractionDao {



    @Update
    abstract fun updateUserInteraction(userInteraction: UserInteraction) : Long?


    abstract fun getFollowing(account: Int, page: Int = 1, pageSize: Int = 50) : List<DisplayUser>


    @Query("SELECT * FROM user_interactions WHERE account = :account AND is_follower = 1 ORDER BY id DESC LIMIT :page OFFSET ((:page - 1) * :pageSize)")
    abstract fun getFollowers(account: Int, page: Int = 1, pageSize: Int = 50) : List<DisplayUser>





    abstract fun deleteAllFollowers()


}