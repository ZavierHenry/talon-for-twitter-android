package com.klinker.android.twitter_l.data.roomdb.daos


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

import com.klinker.android.twitter_l.data.roomdb.entities.Draft

@Dao
abstract class DraftDao {

    fun createDraft(text: String, account: Int) : Draft {
        return Draft(text = text, account = account)
    }

    fun saveDraft(text: String, account: Int) : Draft? {
        return saveDraft(createDraft(text, account))
    }

    fun saveDraft(draft: Draft) : Draft? {
        return insertDraft(draft)?.let { id -> draft.copy(id = id) }
    }

    @Insert
    abstract fun insertDraft(draft: Draft): Long?

    @Delete
    abstract fun deleteDraft(draft: Draft)


    @Query("SELECT * FROM drafts WHERE account = :account ORDER BY id DESC LIMIT :pageSize OFFSET ((:page - 1) * :pageSize)")
    abstract fun getDrafts(account: Int, page: Int = 1, pageSize: Int = 50): List<Draft>


    @Query("DELETE FROM drafts WHERE account = :account")
    abstract fun deleteDrafts(account: Int)

}
