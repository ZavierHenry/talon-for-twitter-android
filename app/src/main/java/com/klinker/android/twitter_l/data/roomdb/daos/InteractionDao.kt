package com.klinker.android.twitter_l.data.roomdb.daos


import com.klinker.android.twitter_l.data.roomdb.entities.Interaction

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
abstract class InteractionDao {


    //TODO: check if there ever would be a conflict
    @Insert
    internal abstract fun insertInteraction(interaction: Interaction)

    @Delete
    internal abstract fun deleteInteraction(interaction: Interaction)

    //TODO: check if update ignores interaction not in table
    @Update
    internal abstract fun updateInteraction(interaction: Interaction)

    @Query("DELETE FROM interactions WHERE id = :id")
    internal abstract fun deleteInteraction(id: Long)

    @Query("DELETE FROM interactions WHERE account = :account")
    internal abstract fun deleteAllInteractions(account: Int)

    //getCursor?
    //getUnreadCursor?

    @Query("SELECT COUNT(id) FROM interactions WHERE account = :account AND is_unread = 1")
    abstract fun getUnreadCount(account: Int): Int

    //TODO: improve query
    @Query("UPDATE interactions SET is_unread = 0 WHERE id IN (SELECT id FROM interactions WHERE account = :account AND is_unread = 1 ORDER BY time DESC LIMIT -1 OFFSET :position)")
    abstract fun markRead(account: Int, position: Int)

    @Query("SELECT users FROM interactions WHERE account = :account AND is_unread = :unread ORDER BY time DESC LIMIT 1 OFFSET :position")
    abstract fun getUsers(account: Int, position: Int, unread: Boolean): String

    @Query("UPDATE interactions SET is_unread = 0 WHERE account = :account")
    abstract fun markAllRead(account: Int)


    @Query("DELETE FROM interactions WHERE id IN(SELECT id FROM interactions WHERE account = :account ORDER BY time DESC LIMIT -1 OFFSET :trimSize)")
    abstract fun trimDatabase(account: Int, trimSize: Int)

    @Query("UPDATE interactions SET interactor_id = :newId WHERE interactor_id = :oldId")
    internal abstract fun changeInteractionUserId(oldId: Long, newId: Long)


}
