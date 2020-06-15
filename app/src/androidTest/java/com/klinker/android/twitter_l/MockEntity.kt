package com.klinker.android.twitter_l

import android.content.ContentValues
import android.database.Cursor
import androidx.sqlite.db.SupportSQLiteQuery

interface MockEntity {
    fun toContentValues() : ContentValues
}