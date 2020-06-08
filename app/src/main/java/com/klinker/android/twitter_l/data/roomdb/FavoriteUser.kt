package com.klinker.android.twitter_l.data.roomdb

import androidx.room.*
import twitter4j.User as TwitterUser


@Entity(tableName = "favorite_users")
data class FavoriteUser(
        @Embedded val user: User,
        val account: Int,
        @PrimaryKey(autoGenerate = true) val id: Long? = null
) {
    constructor(user: TwitterUser, account: Int) : this(User(user), account)
}

@Dao
interface FavoriteUserDao {

    @Insert
    fun insertFavoriteUser(favoriteUser: FavoriteUser) : Long?

    @Delete
    fun deleteFavoriteUser(favoriteUser: FavoriteUser)

    @Query("SELECT * FROM favorite_users WHERE account = :account")
    fun getFavoriteUsers(account: Int) : List<FavoriteUser>

}

