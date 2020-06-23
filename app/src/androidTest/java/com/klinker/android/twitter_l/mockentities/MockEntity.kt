package com.klinker.android.twitter_l.mockentities

import android.content.ContentValues
import android.database.Cursor
import androidx.sqlite.db.SupportSQLiteQuery

interface MockEntity {
    val id: Long?
    fun toContentValues() : ContentValues
}