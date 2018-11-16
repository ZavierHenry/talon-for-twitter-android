package com.klinker.android.twitter_l.data.roomdb.daos


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

import com.klinker.android.twitter_l.data.roomdb.entities.Draft

@Dao
interface DraftDao {

    @Insert
    fun insertDraft(draft: Draft): Long

    @Delete
    fun deleteDraft(draft: Draft)


    @Query("SELECT * FROM drafts WHERE account = :account ORDER BY id DESC")
    fun getDrafts(account: Int): List<Draft>

    @Query("DELETE FROM drafts WHERE account = :account")
    fun deleteDrafts(account: Int)


}
