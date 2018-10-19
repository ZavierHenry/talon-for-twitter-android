package com.klinker.android.twitter_l.data.roomdb;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public abstract class InteractionDao {


    //TODO: check if there ever would be a conflict
    @Insert
    abstract void insertInteraction(Interaction interaction);

    @Delete
    abstract void deleteInteraction(Interaction interaction);

    //TODO: check if update ignores interaction not in table
    @Update
    abstract void updateInteraction(Interaction interaction);

    @Query("DELETE FROM interactions WHERE id = :id")
    abstract void deleteInteraction(long id);

    @Query("DELETE FROM interactions WHERE account = :account")
    abstract void deleteAllInteractions(int account);

    //getCursor?
    //getUnreadCursor?

    @Query("SELECT COUNT(id) FROM interactions WHERE account = :account AND is_unread = 1")
    abstract int getUnreadCount(int account);

    //TODO: improve query
    @Query("UPDATE interactions SET is_unread = 0 WHERE id IN (SELECT id FROM interactions WHERE account = :account ORDER BY time DESC LIMIT -1 OFFSET :position)")
    abstract void markRead(int account, int position);

    @Query("SELECT users FROM interactions WHERE account = :account AND is_unread = :unread ORDER BY time DESC LIMIT 1 OFFSET :position")
    abstract String getUsers(int account, int position, boolean unread);

    @Query("UPDATE interactions SET is_unread = 0 WHERE account = :account")
    abstract void markAllRead(int account);


    @Query("DELETE FROM interactions WHERE account = :account AND id < (SELECT MIN(id) FROM interactions WHERE account = :account ORDER BY time DESC LIMIT :trimSize)")
    abstract void trimDatabase(int account, int trimSize);


}
