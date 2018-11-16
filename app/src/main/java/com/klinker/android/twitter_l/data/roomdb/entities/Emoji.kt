package com.klinker.android.twitter_l.data.roomdb.entities


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(tableName = "emojis",
        indices = [Index(value = ["icon"], unique = true), Index(value = ["text"], unique = true), Index(value = ["count"])])
class Emoji {

    @PrimaryKey(autoGenerate = true)
    val id: Long?


    @ColumnInfo
    var text: String

    @ColumnInfo
    var icon: String

    @ColumnInfo
    var count: Int = 0

    //maybe add account???

    constructor(id: Long, text: String, icon: String, count: Int) {
        this.id = id
        this.text = text
        this.icon = icon
        this.count = count
    }

    @Ignore
    constructor(text: String, icon: String) {
        this.id = null
        this.text = text
        this.icon = icon
        this.count = 0
    }

    fun incrementCount() {
        count += 1
    }

}
