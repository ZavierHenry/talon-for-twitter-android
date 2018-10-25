package com.klinker.android.twitter_l.data.roomdb.daos;


import com.klinker.android.twitter_l.data.roomdb.entities.FavoriteUser;
import com.klinker.android.twitter_l.data.roomdb.entities.User;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.List;

@Dao
public abstract class FavoriteUserDao {


    @Insert
    abstract void insertFavoriteUser(FavoriteUser user);

    @Delete
    abstract void deleteUser(FavoriteUser user);

    @Query("DELETE FROM favorite_users WHERE user_id = :userId AND account = :account")
    abstract void deleteUser(long userId, int account);

    @Query("DELETE FROM favorite_users WHERE account = :account")
    abstract void deleteAllUsers(int account);

    //getCursor implementation
    @Query("SELECT * FROM favorite_users WHERE account = :account")
    abstract List<FavoriteUser> getAllFavoriteUsers(int account);

    @Query("SELECT users.id, name, screen_name, profile_pic, is_verified FROM favorite_users JOIN users ON user_id = users.id AND account = :account AND user_id = :userId")
    abstract User getFavoriteUser(long userId, int account);

    @Query("SELECT users.screen_name FROM favorite_users JOIN users ON favorite_users.user_id = users.id AND account = :account")
    abstract List<String> getFavoriteUserScreenNames(int account);

    @Query("SELECT favorite_users.id FROM favorite_users JOIN users ON favorite_users.user_id = users.id AND users.screen_name = :screenName AND favorite_users.account = :account")
    abstract boolean isFavoriteUser(String screenName, int account);


    @Query("SELECT id FROM favorite_users WHERE user_id = :userId AND account = :account")
    abstract boolean isFavoriteUser(long userId, int account);



}
