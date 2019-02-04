package com.klinker.android.twitter_l.data.roomdb.entities


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

import twitter4j.User as TwitterUser

@Entity(tableName = "users", indices = [Index(value = ["screen_name"]), Index(value = ["twitter_id"])])
data class User(@PrimaryKey(autoGenerate = true) val id: Long? = null,
                @ColumnInfo(name = "twitter_id") val twitterId: Long?,
                @ColumnInfo val name: String,
                @ColumnInfo(name = "screen_name") val screenName: String,
                @ColumnInfo(name = "profile_pic") val profilePic: String,
                @ColumnInfo(name = "is_verified") val isVerified: Boolean) {


    constructor(user: TwitterUser) : this(
            null,
            user.id,
            user.name,
            user.screenName,
            if (user.originalProfileImageURLHttps.isNullOrEmpty())
                user.originalProfileImageURL else user.originalProfileImageURLHttps,
            user.isVerified)

    fun contentEquals(other: User) : Boolean {
        return id == other.id &&
                twitterId == other.twitterId &&
                name.contentEquals(other.name) &&
                screenName.contentEquals(other.screenName) &&
                profilePic.contentEquals(other.profilePic) &&
                isVerified == other.isVerified
    }

}
