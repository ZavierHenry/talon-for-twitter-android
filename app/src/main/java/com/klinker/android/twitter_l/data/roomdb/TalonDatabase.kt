package com.klinker.android.twitter_l.data.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(entities = [
    DirectMessage::class,
    Draft::class,
    Emoji::class,
    FavoriteTweet::class,
    FavoriteUser::class,
    Follower::class,
    Hashtag::class,
    HomeTweet::class,
    ListTweet::class,
    Mention::class,
    QueuedTweet::class,
    SavedTweet::class,
    ScheduledTweet::class,
    UserTweet::class], version = 1)
@TypeConverters(ListStringConverter::class)
abstract class TalonDatabase : RoomDatabase() {

    abstract fun directMessageDao(): DirectMessageDao
    abstract fun draftDao(): DraftDao
    abstract fun emojiDao(): EmojiDao
    abstract fun favoriteTweetDao(): FavoriteTweetDao
    abstract fun favoriteUserDao(): FavoriteUserDao
    abstract fun followerDao(): FollowerDao
    abstract fun hashtagDao() : HashtagDao
    abstract fun homeTweetDao(): HomeTweetDao
    abstract fun listTweetDao(): ListTweetDao
    abstract fun mentionDao(): MentionDao
    abstract fun queuedTweetDao(): QueuedTweetDao
    abstract fun savedTweetDao(): SavedTweetDao
    abstract fun scheduledTweetDao(): ScheduledTweetDao
    abstract fun userTweetDao(): UserTweetDao

    companion object {
        @Volatile private var database: TalonDatabase? = null

        @JvmStatic
        fun getInstance(context: Context) : TalonDatabase {
            val databaseInstance = database
            if (databaseInstance != null) {
                return databaseInstance
            }

            return synchronized(this) {
                val otherDatabaseInstance = database
                if (otherDatabaseInstance != null) {
                    otherDatabaseInstance
                } else {
                    val newDatabase = Room.databaseBuilder(context, TalonDatabase::class.java, "talon-database")
                            .build()
                    database = newDatabase
                    newDatabase
                }
            }
        }

        @JvmStatic
        fun closeDatabase() {
            database?.close()
            database = null
        }
    }
}