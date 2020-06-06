package com.klinker.android.twitter_l.data.roomdb

import androidx.room.ColumnInfo
import twitter4j.User as TwitterUser

data class User(
        @ColumnInfo(name = "screen_name") val screenName: String,
        val name: String? = null,
        @ColumnInfo(name = "profile_pic") val profilePic: String? = null,
        @ColumnInfo(name = "user_id") val userId: Long? = null
) {
    constructor(user: TwitterUser) : this(
            user.screenName,
            user.name,
            user.originalProfileImageURLHttps ?: user.originalProfileImageURL,
            user.id
    )
}