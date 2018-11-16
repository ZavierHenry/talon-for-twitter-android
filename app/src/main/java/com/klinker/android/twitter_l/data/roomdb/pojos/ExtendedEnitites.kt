package com.klinker.android.twitter_l.data.roomdb.pojos

import androidx.room.Embedded
import com.klinker.android.twitter_l.data.roomdb.entities.User

data class ExtendedFavoriteUser(val id: Long?, @Embedded(prefix = "user_") val user: User)

data class ExtendedFollower(val id: Long?, @Embedded(prefix = "user_") val user: User)


