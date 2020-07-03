package com.klinker.android.twitter_l.data.roomdb

import androidx.room.*
import twitter4j.User as TwitterUser


@Entity(tableName = "followers", indices = [Index(value = ["user_id", "account"], unique = true)])
data class Follower @JvmOverloads constructor(
        @Embedded val user: User,
        val account: Int,
        @PrimaryKey(autoGenerate = true) val id: Long = 0
) : BaseDao.TalonEntity<Follower> {
    constructor(user: TwitterUser, account: Int) : this(User(user), account)

    override fun copyWithId(id: Long): Follower {
        return this.copy(id = id)
    }

}

@Dao
abstract class FollowerDao : BaseDao<Follower>() {

    @Query("SELECT * FROM followers WHERE account = :account LIMIT :pageSize OFFSET ((:page - 1) * :pageSize)")
    abstract fun getFollowers(account: Int, page: Int = 1, pageSize: Int = 200) : List<Follower>

    @Ignore
    fun create(user: TwitterUser, account: Int) : Follower {
        return this.insert(Follower(user, account))
    }

}