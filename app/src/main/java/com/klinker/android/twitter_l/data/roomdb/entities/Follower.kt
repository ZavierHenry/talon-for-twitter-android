package com.klinker.android.twitter_l.data.roomdb.entities


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "followers",
        indices = [ Index(value = ["user_id", "account"], unique = true), Index(value = ["account"]) ],
        foreignKeys = [ ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["user_id"], onDelete = ForeignKey.RESTRICT) ])
data class Follower(@PrimaryKey(autoGenerate = true) val id: Long? = null,
               @ColumnInfo val account: Int = -1,
               @ColumnInfo(name = "user_id") val userId: Long = -1) {

    constructor(user: twitter4j.User, account: Int) : this(null, account, user.id)

}