package com.klinker.android.twitter_l.data.roomdb.entities


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_users",
        indices = [Index(value = ["user_id", "account"], unique = true), Index(value = ["account"])],
        foreignKeys = [ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["user_id"], onDelete = ForeignKey.CASCADE)])
class FavoriteUser {

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null

    @ColumnInfo
    var account: Int = 0

    @ColumnInfo(name = "user_id")
    var userId: Long? = null


    constructor(user: twitter4j.User, account: Int) {
        this.id = null
        this.account = account
        this.userId = user.id
    }

    constructor(id: Long, account: Int, userId: Long) {
        this.id = id
        this.account = account
        this.userId = userId
    }

}
