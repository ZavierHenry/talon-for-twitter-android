package com.klinker.android.twitter_l.data.roomdb.entities


import java.util.ArrayList

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "followers",
        indices = [ Index(value = ["user_id", "account"], unique = true), Index(value = ["account"]) ],
        foreignKeys = [ ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["user_id"]) ])
class Follower {

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null

    @ColumnInfo
    var account: Int

    @ColumnInfo(name = "user_id")
    var userId: Long

    constructor(id: Long, account: Int, userId: Long) {

        this.id = id
        this.account = account
        this.userId = userId

    }

    @Ignore
    constructor(account: Int, userId: Long) {
        this.id = null
        this.account = account
        this.userId = userId
    }


    constructor(user: twitter4j.User, account: Int) {
        this.id = null
        this.account = account
        this.userId = user.id
    }


}
