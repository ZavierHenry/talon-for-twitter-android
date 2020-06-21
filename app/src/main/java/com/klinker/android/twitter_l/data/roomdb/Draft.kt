package com.klinker.android.twitter_l.data.roomdb

import androidx.room.*

@Entity(tableName = "drafts")
data class Draft(
        val text: String,
        val account: Int,
        @PrimaryKey(autoGenerate = true) val id: Long? = null
)


@Dao
abstract class DraftDao : BaseDao<Draft>() {

    @Query("DELETE FROM drafts WHERE account = :account")
    abstract fun deleteAllDrafts(account: Int)

    @Query("SELECT * FROM drafts WHERE account = :account LIMIT :pageSize OFFSET ((:page - 1) * :pageSize)")
    abstract fun getDrafts(account: Int, page: Int = 1, pageSize: Int = 200) : List<Draft>

}