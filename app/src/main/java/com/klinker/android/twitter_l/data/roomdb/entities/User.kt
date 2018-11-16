package com.klinker.android.twitter_l.data.roomdb.entities


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey

import twitter4j.User as TwitterUser

@Entity(tableName = "users", indices = [Index(value = ["screen_name"])])
class User {

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null

    @ColumnInfo(name = "twitter_id")
    var twitterId: Long? = null

    @ColumnInfo
    var name: String

    @ColumnInfo(name = "screen_name")
    var screenName: String

    @ColumnInfo(name = "profile_pic")
    var profilePic: String

    @ColumnInfo(name = "is_verified")
    var isVerified: Boolean = false


    constructor(user: TwitterUser) {

        this.id = null
        this.twitterId = user.id
        this.name = user.name
        this.screenName = user.screenName
        this.isVerified = user.isVerified

        if (user.originalProfileImageURLHttps.isNullOrEmpty()) {
            this.profilePic = user.originalProfileImageURL
        } else {
            this.profilePic = user.originalProfileImageURLHttps
        }
    }


    constructor(id: Long, twitterId: Long? = null, name: String, screenName: String, profilePic: String, isVerified: Boolean) {
        this.id = id
        this.name = name
        this.screenName = screenName
        this.profilePic = profilePic
        this.isVerified = isVerified
        this.twitterId = twitterId
    }

    override fun equals(other: Any?): Boolean {
        return other is User
                && this.id == other.id
                && this.name.contentEquals(other.name)
                && this.screenName.contentEquals(other.screenName)
                && this.isVerified == other.isVerified

    }




}
