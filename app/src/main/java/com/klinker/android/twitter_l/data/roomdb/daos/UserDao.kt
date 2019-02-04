package com.klinker.android.twitter_l.data.roomdb.daos


import androidx.room.*
import com.klinker.android.twitter_l.data.roomdb.TalonDatabase
import com.klinker.android.twitter_l.data.roomdb.entities.User

@Dao
abstract class UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    internal abstract fun insertUserInDatabase(user: User) : Long?

    fun insertUser(user: User) : User? {
        return insertUserInDatabase(user).let { id -> if (id != -1L) user.copy(id = id) else null }
    }

    @Update(onConflict = OnConflictStrategy.IGNORE)
    abstract fun updateUser(user: User)

    private fun updateUser(latestUser: User, savedUser: User) {
        if (!latestUser.contentEquals(savedUser)) {
            updateUser(latestUser)
        }
    }

    @Query("DELETE FROM users WHERE id = :id")
    abstract fun deleteUserById(id: Long)

    @Query("SELECT * FROM users WHERE id = :id")
    abstract fun findUserById(id: Long) : User?


    @Query("SELECT * FROM users WHERE screen_name = :screenName")
    abstract fun findUserByScreenName(screenName: String) : User?

    @Query("SELECT * FROM users WHERE twitter_id = :twitterId")
    abstract fun findUserByTwitterId(twitterId: Long): User?

    @Transaction
    open fun findUser(user: User) : User? {

        if (user.id != null) {
            return findUserById(user.id)
        }

        return user.twitterId?.let { findUserByTwitterId(it)} ?: findUserByScreenName(user.screenName)
    }


    @Transaction
    open fun saveUser(user: User): User? {
        return findUser(user)?.also { u -> updateUser(user.copy(id = u.id), u) } ?: insertUser(user)
    }


}
