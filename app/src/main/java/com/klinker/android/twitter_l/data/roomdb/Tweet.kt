package com.klinker.android.twitter_l.data.roomdb

import androidx.room.Embedded

data class Tweet(
        @Embedded val author: User,
        val text: String,
        val time: Long,
        val likes: Int,
        val retweets: Int,
        val hashtags: List<String>,
        val account: Int,
        val liked: Boolean?,
        val retweeted: Boolean?
)