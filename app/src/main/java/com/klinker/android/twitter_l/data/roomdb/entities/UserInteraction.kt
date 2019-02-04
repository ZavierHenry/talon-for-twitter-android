package com.klinker.android.twitter_l.data.roomdb.entities

import android.content.Context
import androidx.room.*
import com.klinker.android.twitter_l.data.roomdb.daos.UserInteractionDao
import twitter4j.Friendship
import twitter4j.User as TwitterUser

@Entity(tableName = "user_interactions",
        indices = [Index(value = ["user_id", "account"], unique = true)],
        foreignKeys = [ForeignKey(entity = User::class, childColumns = ["user_id"], parentColumns = ["id"], onDelete = ForeignKey.CASCADE)])
data class UserInteraction(
        @Embedded(prefix = "user_") val user: User,
        @ColumnInfo val account: Int,
        @PrimaryKey val id: Long? = null,
        @ColumnInfo(name = "is_follower") var isFollower: Boolean = false,
        @ColumnInfo(name = "is_favorite_user") var isFavoriteUser: Boolean = false) {

    constructor(user: TwitterUser, friendship: Friendship, account: Int) : this(User(user), account, id = null, isFollower = friendship.isFollowedBy, isFavoriteUser = friendship.isFollowing)



}


