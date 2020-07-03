package com.klinker.android.twitter_l.data.roomdb

import androidx.room.*
import twitter4j.User as TwitterUser


@Entity(tableName = "favorite_users")
data class FavoriteUser @JvmOverloads constructor(
        @Embedded val user: User,
        val account: Int,
        @PrimaryKey(autoGenerate = true) val id: Long = 0
) : BaseDao.TalonEntity<FavoriteUser> {
    constructor(user: TwitterUser, account: Int) : this(User(user), account)

    override fun copyWithId(id: Long): FavoriteUser {
        return this.copy(id = id)
    }
}

@Dao
abstract class FavoriteUserDao : BaseDao<FavoriteUser>() {

    @Query("SELECT * FROM favorite_users WHERE account = :account LIMIT :pageSize OFFSET ((:page - 1) * :pageSize)")
    abstract fun getFavoriteUsers(account: Int, page: Int = 1, pageSize: Int = 200) : List<FavoriteUser>

    fun create(user: TwitterUser, account: Int) : FavoriteUser {
        return this.insert(FavoriteUser(user, account))
    }

}

