package com.klinker.android.twitter_l.data.roomdb

import androidx.room.*
import twitter4j.User as TwitterUser


@Entity(tableName = "favorite_users")
data class FavoriteUser @JvmOverloads constructor(
        @Embedded val user: User,
        val account: Int,
        @PrimaryKey(autoGenerate = true) val id: Long = 0
) {
    constructor(user: TwitterUser, account: Int) : this(User(user), account)
}

@Dao
abstract class FavoriteUserDao : BaseDao<FavoriteUser>() {

    fun insert(entities: List<FavoriteUser>): List<FavoriteUser> {
        return insertEntities(entities).mapIndexed { index, id ->
            entities[index].copy(id = id)
        }
    }

    fun insert(vararg entities: FavoriteUser): List<FavoriteUser> {
        return insertEntities(*entities).mapIndexed { index, id ->
            entities[index].copy(id = id)
        }
    }

    fun insert(entity: FavoriteUser): FavoriteUser {
        val id = insertEntity(entity)
        return entity.copy(id = id)
    }

    @Query("SELECT * FROM favorite_users WHERE account = :account LIMIT :pageSize OFFSET ((:page - 1) * :pageSize)")
    abstract fun getFavoriteUsers(account: Int, page: Int = 1, pageSize: Int = 200) : List<FavoriteUser>

}

