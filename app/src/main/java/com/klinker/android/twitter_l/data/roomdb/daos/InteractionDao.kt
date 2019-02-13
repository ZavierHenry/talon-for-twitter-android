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


    @Query("DELETE FROM interactions WHERE id IN(SELECT id FROM interactions WHERE account = :account ORDER BY time DESC LIMIT -1 OFFSET :trimSize)")
    abstract fun trimDatabase(account: Int, trimSize: Int)


}
