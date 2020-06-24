package com.klinker.android.twitter_l.data.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(entities = [
    DirectMessage::class,
    Draft::class,
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
        private var database: TalonDatabase? = null

        fun getInstance(context: Context) : TalonDatabase {
            return database ?:
                Room.databaseBuilder(context, TalonDatabase::class.java, "talon-database")
                        .build()
                        .also { database = it }
        }

        fun closeDatabase() {
            database?.close()
            database = null
        }
    }
}