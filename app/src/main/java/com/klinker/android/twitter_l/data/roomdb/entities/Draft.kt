package com.klinker.android.twitter_l.data.roomdb.entities


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "drafts", indices = [ Index(value = ["text"]) ])
data class Draft(@PrimaryKey(autoGenerate = true) val id: Long? = null,
                 @ColumnInfo val text: String,
                 @ColumnInfo val account: Int)
