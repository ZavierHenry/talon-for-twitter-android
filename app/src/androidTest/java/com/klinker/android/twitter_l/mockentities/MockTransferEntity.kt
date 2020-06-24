package com.klinker.android.twitter_l.mockentities

import android.content.ContentValues

interface MockTransferEntity<out T : MockEntity> {
    val mockEntity: T
    fun copyId(id: Long? = null) : MockTransferEntity<T>
    fun toSQLiteContentValues(id: Long? = null): ContentValues
}