package com.klinker.android.twitter_l.data.roomdb.entities


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "hashtags",
        indices = [Index(value = ["name"], unique = true)])
data class Hashtag(@PrimaryKey(autoGenerate = true) val id: Long? = null,
                   @ColumnInfo(name = "name") val tag: String)
