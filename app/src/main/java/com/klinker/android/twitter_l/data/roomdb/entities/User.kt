package com.klinker.android.twitter_l.data.roomdb.entities


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

import twitter4j.User as TwitterUser


data class User(@ColumnInfo(name = "user_twitter_id") val userId: Long?,
                @ColumnInfo val name: String,
                @ColumnInfo(name = "screen_name") val screenName: String,
                @ColumnInfo(name = "profile_pic") val profilePic: String,
                @ColumnInfo(name = "is_verified") val isVerified: Boolean) {


    constructor(user: TwitterUser) : this(
            null,
            user.name,
            user.screenName,
            if (user.originalProfileImageURLHttps.isNullOrEmpty())
                user.originalProfileImageURL else user.originalProfileImageURLHttps,
            user.isVerified)

    fun contentEquals(other: User) : Boolean {
        return userId == other.userId &&
                name.contentEquals(other.name) &&
                screenName.contentEquals(other.screenName) &&
                profilePic.contentEquals(other.profilePic) &&
                isVerified == other.isVerified
    }

}
