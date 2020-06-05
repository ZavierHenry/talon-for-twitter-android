package com.klinker.android.twitter_l.data.roomdb

import androidx.room.ColumnInfo

data class User(
        @ColumnInfo(name = "screen_name") val screenName: String,
        val name: String,
        @ColumnInfo(name = "profile_pic") val profilePic: String,
        val account: Int,
        @ColumnInfo(name = "user_id") val userId: Long? = null
)