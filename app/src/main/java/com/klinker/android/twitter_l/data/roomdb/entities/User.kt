package com.klinker.android.twitter_l.data.roomdb.entities


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "users", indices = [Index(value = ["screen_name"], unique = true)])
class User {

    @PrimaryKey
    var id: Long = 0

    @ColumnInfo
    var name: String

    @ColumnInfo(name = "screen_name")
    var screenName: String

    @ColumnInfo(name = "profile_pic")
    var profilePic: String? = null

    @ColumnInfo(name = "is_verified")
    var isVerified: Boolean = false


    constructor(user: twitter4j.User) {

        id = user.id
        name = user.name
        screenName = user.screenName
        isVerified = user.isVerified

        profilePic = user.originalProfileImageURLHttps ?: user.originalProfileImageURL


    }


    constructor(id: Long, name: String, screenName: String, profilePic: String, isVerified: Boolean) {
        this.id = id
        this.name = name
        this.screenName = screenName
        this.profilePic = profilePic
        this.isVerified = isVerified
    }


}
