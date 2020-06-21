package com.klinker.android.twitter_l.data.roomdb

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

abstract class BaseDao<T> {

    @Insert
    abstract fun insert(entity: T) : Long?

    @Insert
    abstract fun insert(vararg entities: T): List<Long?>

    @Insert
    abstract fun insert(entities: List<T>) : List<Long>

    @Update
    abstract fun update(entity: T)

    @Delete
    abstract fun delete(entity: T)

}