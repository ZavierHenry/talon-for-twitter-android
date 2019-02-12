package com.klinker.android.twitter_l.data.roomdb.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.klinker.android.twitter_l.data.roomdb.entities.UserInteraction
import com.klinker.android.twitter_l.data.roomdb.pojos.DisplayUser


@Dao
abstract class UserInteractionDao {

    @Insert
    abstract fun insertUserInteraction(userInteraction: UserInteraction)

    @Update
    abstract fun updateUserInteraction(userInteraction: UserInteraction) : Int



    //----------------------------------------------------------------------------------------------------------------------------
    //Follower queries


    @Query("SELECT * FROM user_interactions WHERE account = :account ORDER BY user_name DESC LIMIT :pageSize OFFSET ((:page - 1) * :pageSize)")
    abstract fun getFollowers(account: Int, page: Int = 1, pageSize: Int = 100) : List<UserInteraction>

    //Query to search for name with name lowercase and name uppercase
    //abstract fun getFollowers(account: Int, name: String, page: Int = 1, pageSize: Int = 100)


    @Query("UPDATE user_interactions SET is_follower = 0 WHERE account = :account AND is_follower = 1 OR is_follower IS NULL")
    abstract fun deleteAllFollowers(account: Int)


    //----------------------------------------------------------------------------------------------------------------------------
    //Favorite Users queries


    //Query to get favorite user names
    //abstract fun getFavoriteNames() : List<String>




    @Query("SELECT * FROM user_interactions WHERE account = :account ORDER BY user_name DESC LIMIT :pageSize OFFSET ((:page - 1) * :pageSize)")
    abstract fun getFavoriteUsers(account: Int, page: Int = 1, pageSize: Int = 100) : List<UserInteraction>





}