package com.klinker.android.twitter_l.data.roomdb

import androidx.room.*

@Entity(tableName = "favorite_user_notifications", indices = [Index(value = ["tweet_id"], unique = true)])
data class FavoriteUserNotification @JvmOverloads constructor(
        @ColumnInfo(name = "tweet_id") val tweetId: Long,
        @PrimaryKey(autoGenerate = true) val id: Long = 0
) : BaseDao.TalonEntity<FavoriteUserNotification> {

    override fun copyWithId(id: Long): FavoriteUserNotification {
        return this.copy(id = id)
    }

}

@Dao
abstract class FavoriteUserNotificationDao : BaseDao<FavoriteUserNotification>() {

    @Query("DELETE FROM favorite_user_notifications WHERE id NOT IN (SELECT id FROM favorite_user_notifications ORDER BY id DESC LIMIT :size)")
    abstract fun trimTable(size: Int)

    @Query("SELECT id FROM favorite_user_notifications WHERE tweet_id = :tweetId")
    protected abstract fun findNotification(tweetId: Long) : Long

    @Ignore
    fun hasShownNotification(tweetId: Long) : Boolean {
        return findNotification(tweetId) != -1L
    }

    @Ignore
    fun create(tweetId: Long) : FavoriteUserNotification {
        return this.insert(FavoriteUserNotification(tweetId))
    }
}