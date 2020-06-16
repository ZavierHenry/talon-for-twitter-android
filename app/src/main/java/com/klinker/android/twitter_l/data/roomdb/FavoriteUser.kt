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
abstract class FavoriteUserDao {

    @Insert
    abstract fun insertFavoriteUser(favoriteUser: FavoriteUser) : Long?

    @Update
    abstract fun updateFavoriteUser(favoriteUser: FavoriteUser)

    @Delete
    abstract fun deleteFavoriteUser(favoriteUser: FavoriteUser)

    @Query("SELECT * FROM favorite_users WHERE account = :account LIMIT :pageSize OFFSET ((:page - 1) * :pageSize)")
    abstract fun getFavoriteUsers(account: Int, page: Int = 1, pageSize: Int = 200) : List<FavoriteUser>

}

