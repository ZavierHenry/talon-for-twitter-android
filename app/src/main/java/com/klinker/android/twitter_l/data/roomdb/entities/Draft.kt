package com.klinker.android.twitter_l.data.roomdb.entities


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "drafts", indices = [ Index(value = ["text"]) ])
class Draft {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo
    var id: Long?

    @ColumnInfo
    var text: String

    @ColumnInfo
    val account: Int

    @Ignore
    constructor(text: String, account: Int) {
        this.id = null
        this.text = text
        this.account = account
    }

    constructor(id: Long, text: String, account: Int) {
        this.id = id
        this.text = text
        this.account = account
    }

}
