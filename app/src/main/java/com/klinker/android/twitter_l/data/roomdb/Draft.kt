package com.klinker.android.twitter_l.data.roomdb

import androidx.room.*

@Entity(tableName = "drafts")
data class Draft @JvmOverloads constructor(
        val text: String,
        val account: Int,
        @PrimaryKey(autoGenerate = true) val id: Long = 0
)

@Dao
abstract class DraftDao : BaseDao<Draft>() {

    fun insert(entity: Draft): Draft {
        val id = insertEntity(entity)
        return entity.copy(id = id)
    }

    fun insert(vararg entities: Draft): List<Draft> {
        return insertEntities(*entities).mapIndexed { index, id ->
            entities[index].copy(id = id)
        }
    }

    fun insert(entities: List<Draft>): List<Draft> {
        return insertEntities(entities).mapIndexed { index, id ->
            entities[index].copy(id = id)
        }
    }

    @Query("DELETE FROM drafts WHERE account = :account")
    abstract fun deleteAllDrafts(account: Int)

    @Query("SELECT * FROM drafts WHERE account = :account LIMIT :pageSize OFFSET ((:page - 1) * :pageSize)")
    abstract fun getDrafts(account: Int, page: Int = 1, pageSize: Int = 200) : List<Draft>

}