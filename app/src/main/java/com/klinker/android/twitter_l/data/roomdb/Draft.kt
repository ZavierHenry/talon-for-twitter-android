package com.klinker.android.twitter_l.data.roomdb

import androidx.room.*

@Entity(tableName = "drafts")
data class Draft(
        val text: String,
        val account: Int,
        @PrimaryKey(autoGenerate = true) val id: Long? = null
)


@Dao
interface DraftDao {
    @Insert
    fun insertDraft(draft: Draft) : Long?

    @Update
    fun updateDraft(draft: Draft)

    @Delete
    fun deleteDraft(draft: Draft)

    @Query("DELETE FROM drafts")
    fun deleteAllDrafts()

    @Query("SELECT * FROM drafts WHERE account = :account LIMIT :pageSize OFFSET ((:page - 1) * :pageSize)")
    fun getDrafts(account: Int, page: Int = 1, pageSize: Int = 200) : List<Draft>

}