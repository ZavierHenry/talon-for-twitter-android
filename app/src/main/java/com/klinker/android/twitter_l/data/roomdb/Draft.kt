package com.klinker.android.twitter_l.data.roomdb

import androidx.room.*

@Entity(tableName = "drafts")
data class Draft(
        val text: String,
        val account: Int,
        @PrimaryKey(autoGenerate = true) var id: Int? = null
)


@Dao
interface DraftDao {
    @Insert
    fun insertDraft(draft: Draft) : Int

    @Delete
    fun deleteDraft(draft: Draft)

    //delete all drafts

    @Query("SELECT * FROM drafts WHERE account = :account")
    fun getDrafts(account: Int) : List<Draft>


}