package com.klinker.android.twitter_l.data.roomdb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Draft(
        @PrimaryKey val id: Int,
        val account: Int,
        val text: String
)