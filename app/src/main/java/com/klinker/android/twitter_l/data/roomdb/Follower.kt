package com.klinker.android.twitter_l.data.roomdb

import androidx.room.*
import twitter4j.User as TwitterUser


@Entity(tableName = "followers")
data class Follower @JvmOverloads constructor(
        @Embedded val user: User,
        val account: Int,
        @PrimaryKey(autoGenerate = true) val id: Long = 0
) {
    constructor(user: TwitterUser, account: Int) : this(User(user), account)
}

@Dao
abstract class FollowerDao : BaseDao<Follower>() {

    fun insert(entities: List<Follower>): List<Follower> {
        return insertEntities(entities).mapIndexed { index, id ->
            entities[index].copy(id = id)
        }
    }

    fun insert(vararg entities: Follower): List<Follower> {
        return insertEntities(*entities).mapIndexed { index, id ->
            entities[index].copy(id = id)
        }
    }

    fun insert(entity: Follower): Follower {
        val id = insertEntity(entity)
        return entity.copy(id = id)
    }

    @Query("SELECT * FROM followers WHERE account = :account LIMIT :pageSize OFFSET ((:page - 1) * :pageSize)")
    abstract fun getFollowers(account: Int, page: Int = 1, pageSize: Int = 200) : List<Follower>

}