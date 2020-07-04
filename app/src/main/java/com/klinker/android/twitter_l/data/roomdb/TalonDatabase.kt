package com.klinker.android.twitter_l.data.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.klinker.android.twitter_l.adapters.TimelinePagerAdapter
import com.klinker.android.twitter_l.settings.AppSettings


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

    fun trimTables(context: Context, account: Int) {
        val settings = AppSettings.getInstance(context)
        val sharedPreferences = AppSettings.getSharedPreferences(context)

        //trim interactions table
        homeTweetDao().trimDatabase(account, settings.timelineSize)
        //trim favorite user notifications table
        for (accountIndex in 0..1) {
            for (i in 1..TimelinePagerAdapter.MAX_EXTRA_PAGES) {
                val listId = sharedPreferences.getLong("account_${accountIndex}_list_${i}_long", 0)
                val userId = sharedPreferences.getLong("account_${accountIndex}_user_tweets_${i}_long", 0)
                listTweetDao().trimDatabase(listId, settings.listSize)
                userTweetDao().trimDatabase(userId, settings.userTweetsSize)
            }
        }

        mentionDao().trimDatabase(account, settings.mentionsSize)
        directMessageDao().trimDatabase(account, settings.dmSize)
        hashtagDao().trimDatabase(500)
        //trim activity table
        favoriteTweetDao().trimDatabase(account, 400)

    }

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