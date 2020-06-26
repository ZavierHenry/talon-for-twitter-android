package com.klinker.android.twitter_l.mockentities

import android.content.ContentValues

interface MockTransferEntity<out T : MockEntity> {
    val mockEntity: T
    fun copyId(id: Long = 0) : MockTransferEntity<T>
    fun toSQLiteContentValues(id: Long = 0): ContentValues
}