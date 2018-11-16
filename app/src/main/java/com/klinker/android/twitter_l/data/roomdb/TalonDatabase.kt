package com.klinker.android.twitter_l.data.roomdb

import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.RemoteCallbackList
import android.os.strictmode.SqliteObjectLeakedViolation
import android.telecom.Call
import android.util.Log

import com.klinker.android.twitter_l.data.roomdb.entities.Activity
import com.klinker.android.twitter_l.data.roomdb.entities.DirectMessage
import com.klinker.android.twitter_l.data.roomdb.entities.Draft
import com.klinker.android.twitter_l.data.roomdb.entities.Emoji
import com.klinker.android.twitter_l.data.roomdb.entities.FavoriteTweet
import com.klinker.android.twitter_l.data.roomdb.entities.FavoriteUser
import com.klinker.android.twitter_l.data.roomdb.entities.FavoriteUserNotification
import com.klinker.android.twitter_l.data.roomdb.entities.Follower
import com.klinker.android.twitter_l.data.roomdb.entities.Hashtag
import com.klinker.android.twitter_l.data.roomdb.entities.HomeTweet
import com.klinker.android.twitter_l.data.roomdb.entities.Interaction
import com.klinker.android.twitter_l.data.roomdb.entities.ListTweet
import com.klinker.android.twitter_l.data.roomdb.entities.Mention
import com.klinker.android.twitter_l.data.roomdb.entities.QueuedTweet
import com.klinker.android.twitter_l.data.roomdb.entities.SavedTweet
import com.klinker.android.twitter_l.data.roomdb.entities.ScheduledTweet
import com.klinker.android.twitter_l.data.roomdb.entities.Tweet
import com.klinker.android.twitter_l.data.roomdb.entities.User
import com.klinker.android.twitter_l.data.roomdb.entities.UserTweet
import com.klinker.android.twitter_l.data.sq_lite.ActivitySQLiteHelper
import com.klinker.android.twitter_l.data.sq_lite.DMSQLiteHelper
import com.klinker.android.twitter_l.data.sq_lite.EmojiSQLiteHelper
import com.klinker.android.twitter_l.data.sq_lite.FavoriteTweetsSQLiteHelper
import com.klinker.android.twitter_l.data.sq_lite.FavoriteUserNotificationSQLiteHelper
import com.klinker.android.twitter_l.data.sq_lite.FavoriteUsersSQLiteHelper
import com.klinker.android.twitter_l.data.sq_lite.FollowersSQLiteHelper
import com.klinker.android.twitter_l.data.sq_lite.HashtagSQLiteHelper
import com.klinker.android.twitter_l.data.sq_lite.HomeSQLiteHelper
import com.klinker.android.twitter_l.data.sq_lite.InteractionsSQLiteHelper
import com.klinker.android.twitter_l.data.sq_lite.ListSQLiteHelper
import com.klinker.android.twitter_l.data.sq_lite.MentionsSQLiteHelper
import com.klinker.android.twitter_l.data.sq_lite.QueuedSQLiteHelper
import com.klinker.android.twitter_l.data.sq_lite.SavedTweetSQLiteHelper
import com.klinker.android.twitter_l.data.sq_lite.UserTweetsSQLiteHelper

import java.io.File
import java.sql.SQLInput
import java.util.HashMap
import java.util.HashSet
import java.util.Queue
import java.util.concurrent.atomic.AtomicLong
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.klinker.android.twitter_l.data.roomdb.daos.*

@Database(entities = [
    Activity::class,
    DirectMessage::class,
    Draft::class,
    Emoji::class,
    FavoriteTweet::class,
    FavoriteUser::class,
    FavoriteUserNotification::class,
    Follower::class,
    Hashtag::class,
    HomeTweet::class,
    Interaction::class,
    ListTweet::class,
    Mention::class,
    QueuedTweet::class,
    SavedTweet::class,
    ScheduledTweet::class,
    Tweet::class,
    User::class,
    UserTweet::class ], version = 1, exportSchema = false)
abstract class TalonDatabase : RoomDatabase() {

    abstract fun activityDao(): ActivityDao
    abstract fun directMessageDao(): DirectMessageDao
    abstract fun draftDao(): DraftDao
    abstract fun emojiDao(): EmojiDao
    abstract fun favoriteTweetDao(): FavoriteTweetDao
    abstract fun favoriteUserDao(): FavoriteUserDao
    abstract fun favoriteUserNotificationDao(): FavoriteUserNotificationDao
    abstract fun followerDao(): FollowerDao
    abstract fun hashtagDao(): HashtagDao
    abstract fun homeTweetDao(): HomeTweetDao
    abstract fun interactionDao(): InteractionDao
    abstract fun listTweetDao(): ListTweetDao
    abstract fun mentionDao(): MentionDao
    abstract fun queuedTweetDao(): QueuedTweetDao
    abstract fun savedTweetDao(): SavedTweetDao
    abstract fun scheduledTweetDao(): ScheduledTweetDao
    abstract fun userTweetDao(): UserTweetDao
    abstract fun userDao() : UserDao
    abstract fun tweetDao() : TweetDao


    //delete databases
    //TODO: rewrite callbacks to facilitate testing

    private abstract class TransferCallback constructor(private val databasePath: String) : RoomDatabase.Callback() {

        internal abstract fun readDatabase(db: SupportSQLiteDatabase, source: SQLiteDatabase)

        override fun onCreate(db: SupportSQLiteDatabase) {
            getDatabase(databasePath)?.use { source ->
                readDatabase(db, source)
            }
        }

        private fun getDatabase(absolutePath: String): SQLiteDatabase? {

            try {
                return SQLiteDatabase.openDatabase(absolutePath, null, SQLiteDatabase.OPEN_READONLY)
            } catch (e: Exception) {
                return null
            }

        }

    }


    private fun mergeTweets(newTweet: Tweet, targetTweet: Tweet) {

    }

    private fun mergeUsers(newUser: User, targetUser: User) {

        if (targetUser.id!! < 0) {
            targetUser.id = newUser.id
        }

        if (targetUser.profilePic.isNullOrEmpty()) {
            targetUser.profilePic = newUser.profilePic
        }

        if (targetUser.name.isNullOrEmpty()) {
            targetUser.name = newUser.name
        }

    }

    private fun fillUserContentValues(user: User, contentValues: ContentValues) {
        contentValues.put("id", user.id)
        contentValues.put("name", user.name)
        contentValues.put("screen_name", user.screenName)
        contentValues.put("profile_pic", user.profilePic)
        contentValues.put("is_verified", user.isVerified)
    }

    private fun fillTweetContentValues(tweet: Tweet, contentValues: ContentValues) {

    }


    companion object {

        @Volatile private var instance: TalonDatabase? = null

        @JvmStatic fun getInstance(context: Context): TalonDatabase {

            val inst = instance
            if (inst != null) {
                return inst
            }

            return synchronized(this) {
                val inst2 = instance
                if (inst2 != null) {
                    inst2
                } else {
                    val createdInstance = buildDatabase(context)
                    instance = createdInstance
                    createdInstance
                }
            }

        }

        private fun buildDatabase(context: Context): TalonDatabase {
            val userId = AtomicLong(-2L)
            val users = HashMap<String, User>()


            val dmDbPath = context.getDatabasePath(DMSQLiteHelper.DATABASE_NAME).absolutePath
            val emojiDbPath = context.getDatabasePath(EmojiSQLiteHelper.DATABASE_NAME).absolutePath
            val favoriteTweetsDbPath = context.getDatabasePath(FavoriteTweetsSQLiteHelper.DATABASE_NAME).absolutePath
            val favoriteUserNotificationsDbPath = context.getDatabasePath(FavoriteUserNotificationSQLiteHelper.DATABASE_NAME).absolutePath
            val favoriteUsersDbPath = context.getDatabasePath(FavoriteUsersSQLiteHelper.DATABASE_NAME).absolutePath
            val followersUsersDbPath = context.getDatabasePath(FollowersSQLiteHelper.DATABASE_NAME).absolutePath
            val hashtagDbPath = context.getDatabasePath(HashtagSQLiteHelper.DATABASE_NAME).absolutePath
            val homeTweetsDbPath = context.getDatabasePath(HomeSQLiteHelper.DATABASE_NAME).absolutePath
            val interactionsDbPath = context.getDatabasePath(InteractionsSQLiteHelper.DATABASE_NAME).absolutePath
            val listDbPath = context.getDatabasePath(ListSQLiteHelper.DATABASE_NAME).absolutePath
            val mentionsDbPath = context.getDatabasePath(MentionsSQLiteHelper.DATABASE_NAME).absolutePath
            val queuedDbPath = context.getDatabasePath(QueuedSQLiteHelper.DATABASE_NAME).absolutePath
            val savedTweetsDbPath = context.getDatabasePath(SavedTweetSQLiteHelper.DATABASE_NAME).absolutePath
            val userTweetsDbPath = context.getDatabasePath(UserTweetsSQLiteHelper.DATABASE_NAME).absolutePath


             return Room.databaseBuilder(context.applicationContext, TalonDatabase::class.java, "talondata.db")
                     .addCallback(transferActivityData(getDatabasePath(context, ActivitySQLiteHelper.DATABASE_NAME), userId))
                     .addCallback(transferDirectMessageData(dmDbPath, userId))
                     .addCallback(transferEmojiData(emojiDbPath))
                     .addCallback(transferFavoriteTweetsData(favoriteTweetsDbPath, userId))
                     .addCallback(transferFavoriteUserNotificationsData(favoriteUserNotificationsDbPath))
                     .addCallback(transferFavoriteUsersData(favoriteUsersDbPath, users))
                     .addCallback(transferFollowersData(followersUsersDbPath, userId))
                     .addCallback(transferHashtagData(hashtagDbPath))
                     .addCallback(transferHomeTweetsData(homeTweetsDbPath))
                     .addCallback(transferInteractionsData(interactionsDbPath))
                     .addCallback(transferListData(listDbPath))
                     .addCallback(transferMentionsData(mentionsDbPath, userId))
                     .addCallback(transferQueuedData(queuedDbPath))
                     .addCallback(transferSavedTweetsData(savedTweetsDbPath, userId))
                     .addCallback(transferUserTweetsData(userTweetsDbPath))
                     .build()
        }

        private fun getDatabasePath(context: Context, relativePath: String): String {
            return context.getDatabasePath(relativePath).absolutePath
        }


        @JvmStatic fun transferActivityData(absolutePath: String, userIdLabeler: AtomicLong): RoomDatabase.Callback {
            return object : TransferCallback(absolutePath) {
                override fun readDatabase(db: SupportSQLiteDatabase, source: SQLiteDatabase) {

                }
            }
        }

        @JvmStatic fun transferDirectMessageData(absolutePath: String, userIdLabeler: AtomicLong): RoomDatabase.Callback {
            return object : TransferCallback(absolutePath) {
                override fun readDatabase(db: SupportSQLiteDatabase, source: SQLiteDatabase) {

                }
            }
        }

        @JvmStatic fun transferEmojiData(absolutePath: String): RoomDatabase.Callback {
            return object : TransferCallback(absolutePath) {
                override fun readDatabase(db: SupportSQLiteDatabase, source: SQLiteDatabase) {

                    source.query(EmojiSQLiteHelper.TABLE_RECENTS, null, null, null, null, null, EmojiSQLiteHelper.COLUMN_COUNT + " DESC").use { cursor ->
                        if (cursor != null && cursor.moveToFirst()) {
                            val contentValues = ContentValues()

                            do {
                                val text = cursor.getString(cursor.getColumnIndex(EmojiSQLiteHelper.COLUMN_TEXT))
                                val icon = cursor.getString(cursor.getColumnIndex(EmojiSQLiteHelper.COLUMN_ICON))
                                val count = cursor.getInt(cursor.getColumnIndex(EmojiSQLiteHelper.COLUMN_COUNT))

                                contentValues.put("text", text)
                                contentValues.put("icon", icon)
                                contentValues.put("count", count)

                                db.insert("emojis", SQLiteDatabase.CONFLICT_IGNORE, contentValues)

                            } while (cursor.moveToNext())

                        }
                    }

                }
            }

        }

        @JvmStatic fun transferFavoriteTweetsData(absolutePath: String, userIdLabeler: AtomicLong): RoomDatabase.Callback {
            return object : TransferCallback(absolutePath) {
                override fun readDatabase(db: SupportSQLiteDatabase, source: SQLiteDatabase) {
                    val tweetIds = HashSet<Long>()


                }
            }
        }

        @JvmStatic fun transferFavoriteUserNotificationsData(absolutePath: String): RoomDatabase.Callback {
            return object : TransferCallback(absolutePath) {
                override fun readDatabase(db: SupportSQLiteDatabase, source: SQLiteDatabase) {

                }
            }
        }


        @JvmStatic fun transferFavoriteUsersData(absolutePath: String, users: Map<String, User>): RoomDatabase.Callback {
            return object : TransferCallback(absolutePath) {
                override fun readDatabase(db: SupportSQLiteDatabase, source: SQLiteDatabase) {

                }
            }
        }


        @JvmStatic fun transferFollowersData(absolutePath: String, userLabeler: AtomicLong): RoomDatabase.Callback {
            return object : TransferCallback(absolutePath) {
                override fun readDatabase(db: SupportSQLiteDatabase, source: SQLiteDatabase) {
                    val enteredUsers = HashMap<String, Long>()
                    db.query("SELECT id, screen_name FROM hashtags").use { cursor ->
                        if (cursor != null && cursor.moveToFirst()) {
                            do {
                                val screenName = cursor.getString(cursor.getColumnIndex(FollowersSQLiteHelper.COLUMN_SCREEN_NAME))
                                val id = cursor.getLong(cursor.getColumnIndex(FollowersSQLiteHelper.COLUMN_ID))

                            } while (cursor.moveToNext())
                        }
                    }


                }
            }
        }

        @JvmStatic fun transferHashtagData(absolutePath: String): RoomDatabase.Callback {
            return object : TransferCallback(absolutePath) {
                public override fun readDatabase(db: SupportSQLiteDatabase, source: SQLiteDatabase) {

                    source.query(HashtagSQLiteHelper.TABLE_HASHTAGS, arrayOf(HashtagSQLiteHelper.COLUMN_TAG), null, null, null, null, null).use { cursor ->

                        if (cursor != null && cursor.moveToFirst()) {
                            val contentValues = ContentValues()

                            db.beginTransaction()

                            do {

                                val name = cursor.getString(cursor.getColumnIndex(HashtagSQLiteHelper.COLUMN_TAG))
                                contentValues.put("name", name)
                                db.insert("hashtags", SQLiteDatabase.CONFLICT_IGNORE, contentValues)

                            } while (cursor.moveToNext())

                            db.setTransactionSuccessful()
                            db.endTransaction()
                        }

                    }
                }
            }
        }

        @JvmStatic fun transferHomeTweetsData(absolutePath: String): RoomDatabase.Callback {
            return object : TransferCallback(absolutePath) {
                override fun readDatabase(db: SupportSQLiteDatabase, source: SQLiteDatabase) {

                }
            }
        }


        @JvmStatic fun transferInteractionsData(absolutePath: String): RoomDatabase.Callback {
            return object : TransferCallback(absolutePath) {
                override fun readDatabase(db: SupportSQLiteDatabase, source: SQLiteDatabase) {

                }
            }
        }


        @JvmStatic fun transferListData(absolutePath: String): RoomDatabase.Callback {
            return object : TransferCallback(absolutePath) {
                override fun readDatabase(db: SupportSQLiteDatabase, source: SQLiteDatabase) {

                }
            }
        }


        @JvmStatic fun transferMentionsData(absolutePath: String, userIdLabeler: AtomicLong): RoomDatabase.Callback {
            return object : TransferCallback(absolutePath) {
                override fun readDatabase(db: SupportSQLiteDatabase, source: SQLiteDatabase) {

                }
            }
        }


        @JvmStatic fun transferQueuedData(absolutePath: String): RoomDatabase.Callback {
            return object : TransferCallback(absolutePath) {
                override fun readDatabase(db: SupportSQLiteDatabase, source: SQLiteDatabase) {

                    source.query(QueuedSQLiteHelper.TABLE_QUEUED, null, null, null, null, null, null).use { cursor ->

                        if (cursor != null && cursor.moveToFirst()) {
                            val draftContentValues = ContentValues()
                            val scheduledTweetContentValues = ContentValues()
                            val queuedTweetContentValues = ContentValues()

                            db.beginTransaction()

                            do {

                                val type = cursor.getInt(cursor.getColumnIndex(QueuedSQLiteHelper.COLUMN_TYPE))
                                val alarmId = cursor.getLong(cursor.getColumnIndex(QueuedSQLiteHelper.COLUMN_ALARM_ID))
                                val text = cursor.getString(cursor.getColumnIndex(QueuedSQLiteHelper.COLUMN_TEXT))
                                val account = cursor.getInt(cursor.getColumnIndex(QueuedSQLiteHelper.COLUMN_ACCOUNT))
                                val time = cursor.getLong(cursor.getColumnIndex(QueuedSQLiteHelper.COLUMN_TIME))

                                when (type) {
                                    QueuedSQLiteHelper.TYPE_DRAFT -> {
                                        draftContentValues.put("text", text)
                                        draftContentValues.put("account", account)
                                        db.insert("drafts", SQLiteDatabase.CONFLICT_IGNORE, draftContentValues)
                                    }

                                    QueuedSQLiteHelper.TYPE_SCHEDULED -> {
                                        scheduledTweetContentValues.put("alarm_id", alarmId)
                                        scheduledTweetContentValues.put("text", text)
                                        scheduledTweetContentValues.put("account", account)
                                        scheduledTweetContentValues.put("time", time)
                                        db.insert("scheduled_tweets", SQLiteDatabase.CONFLICT_IGNORE, scheduledTweetContentValues)
                                    }

                                    QueuedSQLiteHelper.TYPE_QUEUED_TWEET -> {
                                        queuedTweetContentValues.put("text", text)
                                        queuedTweetContentValues.put("account", account)
                                        db.insert("queued_tweets", SQLiteDatabase.CONFLICT_IGNORE, queuedTweetContentValues)
                                    }
                                }

                            } while (cursor.moveToNext())

                            db.setTransactionSuccessful()
                            db.endTransaction()

                        }

                    }

                }
            }
        }


        fun transferSavedTweetsData(absolutePath: String, userLabeler: AtomicLong): RoomDatabase.Callback {
            return object : TransferCallback(absolutePath) {
                override fun readDatabase(db: SupportSQLiteDatabase, source: SQLiteDatabase) {

                }
            }
        }


        fun transferUserTweetsData(absolutePath: String): RoomDatabase.Callback {
            return object : TransferCallback(absolutePath) {
                override fun readDatabase(db: SupportSQLiteDatabase, source: SQLiteDatabase) {

                }
            }
        }


        //TODO: possibly redo this to be thread safe
        fun destroyInstance() {
            instance?.close()
            instance = null
        }
    }


}
