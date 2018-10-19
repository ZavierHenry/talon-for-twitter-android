package com.klinker.android.twitter_l.data.roomdb;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface FavoriteUserDao {


    @Insert
    void insertFavoriteUser(FavoriteUser user);

    @Query("DELETE FROM favorite_users WHERE user_id = :userId AND account = :account")
    void deleteUser(long userId, int account);

    @Query("DELETE FROM favorite_users WHERE account = :account")
    void deleteAllUsers(int account);

    //getCursor implementation
    @Query("SELECT * FROM favorite_users WHERE account = :account")
    List<FavoriteUser> getAllFavoriteUsers(int account);

    @Query("SELECT users.screen_name FROM favorite_users JOIN users ON favorite_users.user_id = users.id WHERE account = :account")
    List<String> getFavoriteUserScreenNames(int account);

    @Query("SELECT favorite_users.id FROM favorite_users JOIN users ON favorite_users.user_id = users.id AND users.screen_name = :screenName WHERE favorite_users.account = :account")
    boolean isFavoriteUser(String screenName, int account);


    @Query("SELECT id FROM favorite_users WHERE user_id = :userId AND account = :account")
    boolean isFavoriteUser(long userId, int account);



}
