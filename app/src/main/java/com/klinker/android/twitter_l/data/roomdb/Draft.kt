package com.klinker.android.twitter_l.data.roomdb

import androidx.room.*

@Entity(tableName = "drafts")
data class Draft @JvmOverloads constructor(
        val text: String,
        val account: Int,
        @PrimaryKey(autoGenerate = true) val id: Long = 0
) : BaseDao.TalonEntity<Draft> {

    override fun copyWithId(id: Long): Draft {
        return this.copy(id = id)
    }

}

@Dao
abstract class DraftDao : BaseDao<Draft>() {

    @Query("DELETE FROM drafts WHERE account = :account")
    abstract fun deleteAllDrafts(account: Int)

    @Query("SELECT * FROM drafts WHERE account = :account LIMIT :pageSize OFFSET ((:page - 1) * :pageSize)")
    abstract fun getDrafts(account: Int, page: Int = 1, pageSize: Int = 200) : List<Draft>

    @Ignore
    fun create(text: String, account: Int) : Draft {
        return this.insert(Draft(text, account))
    }
}