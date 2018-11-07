package com.klinker.android.twitter_l.data.roomdb.entities


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "hashtags",
        indices = [Index(value = ["name"], unique = true)])
class Hashtag {

    @PrimaryKey(autoGenerate = true)
    val id: Long?

    @ColumnInfo(name = "name")
    var tag: String

    //maybe add account?

    constructor(id: Long, tag: String) {
        this.id = id
        this.tag = tag
    }

    @Ignore
    constructor(tag: String) {
        this.id = null
        this.tag = tag
    }

}
