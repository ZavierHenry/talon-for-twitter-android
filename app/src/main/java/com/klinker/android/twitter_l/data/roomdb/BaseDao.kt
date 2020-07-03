package com.klinker.android.twitter_l.data.roomdb

import androidx.room.*

abstract class BaseDao<T> where T : BaseDao.TalonEntity<T> {

    interface TalonEntity<T>  {
        fun copyWithId(id: Long) : T
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    protected abstract fun insertEntity(entity: T) : Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    protected abstract fun insertEntities(vararg entities: T): List<Long>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    protected abstract fun insertEntities(entities: List<T>) : List<Long>

    @Ignore
    fun insert(entity: T): T {
        val id = insertEntity(entity)
        return entity.copyWithId(id)
    }

    @Ignore
    fun insert(vararg entities: T) : List<T> {
        return insertEntities(*entities).mapIndexed { index, id ->
            entities[index].copyWithId(id)
        }
    }

    @Ignore
    fun insert(entities: List<T>) : List<T> {
        return insertEntities(entities).mapIndexed { index, id ->
            entities[index].copyWithId(id)
        }
    }

    @Update(onConflict = OnConflictStrategy.IGNORE)
    abstract fun update(entity: T) : Int

    @Delete
    abstract fun delete(entity: T) : Int

}

