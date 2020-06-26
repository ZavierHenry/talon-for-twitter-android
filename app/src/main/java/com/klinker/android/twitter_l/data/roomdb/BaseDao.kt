package com.klinker.android.twitter_l.data.roomdb

import androidx.room.*

abstract class BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertEntity(entity: T) : Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertEntities(vararg entities: T): List<Long>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertEntities(entities: List<T>) : List<Long>

    @Update(onConflict = OnConflictStrategy.IGNORE)
    abstract fun update(entity: T)

    @Delete
    abstract fun delete(entity: T)

}