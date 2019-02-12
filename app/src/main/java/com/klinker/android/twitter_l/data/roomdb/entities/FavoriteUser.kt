package com.klinker.android.twitter_l.data.roomdb.entities


import androidx.room.*

import twitter4j.User as TwitterUser


@Entity(tableName = "favorite_users",
        indices = [Index(value = ["user_id", "account"], unique = true), Index(value = ["account"])],
        foreignKeys = [ForeignKey(entity = UserInteraction::class, parentColumns = ["id"], childColumns = ["user_id"], onDelete = ForeignKey.CASCADE)])
data class FavoriteUser(@PrimaryKey(autoGenerate = true) val id: Long? = null,
                        @ColumnInfo val account: Int = 1,
                        @ColumnInfo(name = "user_id") val userId: Long) {

    constructor(id: Long? = null, account: Int, user: User) : this(id, account, user.userId!!)

}




//@Entity(tableName = "favorite_users",
//        indices = [Index(value = ["user_id", "account"], unique = true), Index(value = ["account"])],
//        foreignKeys = [ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["user_id"], onDelete = ForeignKey.CASCADE)])
//class FavoriteUser {
//
//    @PrimaryKey(autoGenerate = true)
//    var id: Long? = null
//
//    @ColumnInfo
//    var account: Int = 0
//
//    @ColumnInfo(name = "user_id")
//    var userId: Long? = null
//
//
//    constructor(user: TwitterUser, account: Int) {
//        this.id = null
//        this.account = account
//        this.userId = user.id
//    }
//
//    constructor(account: Int, userId: Long, id: Long? = null) {
//        this.id = id
//        this.account = account
//        this.userId = userId
//    }
//
//}