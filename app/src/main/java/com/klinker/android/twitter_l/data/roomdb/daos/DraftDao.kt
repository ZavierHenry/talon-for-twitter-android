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

    @Query("DELETE FROM drafts WHERE id = :id")
    fun deleteDraft(id: Long)

    @Query("DELETE FROM drafts WHERE text = :message")
    fun deleteDraft(message: String)


    @Query("SELECT * FROM drafts WHERE account = :account")
    fun getDrafts(account: Int): List<Draft>


}
