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
import com.klinker.android.twitter_l.data.roomdb.entities.*
import com.klinker.android.twitter_l.settings.AppSettings

@Database(entities = [

    TweetInteraction::class,
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

    abstract fun tweetInteractionDao() : TweetInteractionDao


    //delete databases
    //TODO: rewrite callbacks to facilitate testing

    private abstract class TransferCallback constructor(private val databasePath: String,
                                                        private val context: Context) : RoomDatabase.Callback() {


        internal abstract fun readDatabase(context: Context, db: SupportSQLiteDatabase, source: SQLiteDatabase)

        override fun onCreate(db: SupportSQLiteDatabase) {
            getDatabase(databasePath)?.use { source ->
                readDatabase(context, db, source)
            }
        }

        private fun getDatabase(absolutePath: String): SQLiteDatabase? = try {
            SQLiteDatabase.openDatabase(absolutePath, null, SQLiteDatabase.OPEN_READONLY)
        } catch (e: Exception) {
            null
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

        @JvmStatic private fun getStringFromCursor(cursor: Cursor, columnName: String) : String? {
            return cursor.getColumnIndex(columnName).let { index ->
                if (index != -1) cursor.getString(index) else null
            }
        }

        @JvmStatic private fun getIntFromCursor(cursor: Cursor, columnName: String) : Int? {
            return cursor.getColumnIndex(columnName).let { index ->
                if (index != -1) cursor.getInt(index) else null
            }
        }

        @JvmStatic private fun getBooleanFromCursor(cursor: Cursor, columnName: String) : Boolean? {
            return getIntFromCursor(cursor, columnName)?.let { if (it != -1) it == 1 else null }
        }

        @JvmStatic private fun getLongFromCursor(cursor: Cursor, columnName: String) : Long? {
            return cursor.getColumnIndex(columnName).let { index ->
                if (index != -1) cursor.getLong(index) else null
            }
        }

        @JvmStatic private fun <T> removeFromContentValues(contentValues: ContentValues, key: String, value: T?, comparisonValue: T) {
            if (value != null && value != comparisonValue) {
                contentValues.remove(key)
            }
        }


        @JvmStatic private fun mergeUsers(cursor: Cursor, contentValues: ContentValues) : ContentValues {
            val twitterId = getLongFromCursor(cursor, "twitter_id")
            val name = getStringFromCursor(cursor, "name")
            val screenName = getStringFromCursor(cursor, "screen_name")
            val profilePic = getStringFromCursor(cursor, "profile_pic")
            val isVerified = getBooleanFromCursor(cursor, "is_verified")

            return ContentValues().apply {
                putAll(contentValues)
                removeFromContentValues(this, "twitter_id", twitterId, -1L)
                removeFromContentValues(this, "name", name, "")
                removeFromContentValues(this, "screen_name", screenName, "")
                removeFromContentValues(this, "profile_pic", profilePic, "")
                if (isVerified != null) remove("is_verified")
            }

        }

        @JvmStatic private fun mergeTweets(cursor: Cursor, contentValues: ContentValues) : ContentValues {
            val userId = getLongFromCursor(cursor,"user_id")
            val time = getLongFromCursor(cursor, "time")
            val text = getStringFromCursor(cursor, "text")
            val likeCount = getIntFromCursor(cursor, "like_count")
            val retweetCount = getIntFromCursor(cursor, "retweet_count")
            val gifUrl = getStringFromCursor(cursor, "gif_url")
            val mediaDuration = getIntFromCursor(cursor, "media_duration")
            val pictureUrls = getStringFromCursor(cursor, "picture_urls")
            val urls = getStringFromCursor(cursor, "urls")
            val hashtags = getStringFromCursor(cursor, "hashtags")
            val users = getStringFromCursor(cursor, "users")
            val isConversation = getBooleanFromCursor(cursor, "is_conversation")
            val retweeter = getStringFromCursor(cursor, "retweeter")
            val clientSource = getStringFromCursor(cursor, "client_source")




            return ContentValues().apply {
                putAll(contentValues)
                removeFromContentValues(this, "user_id", userId, -1L)
                removeFromContentValues(this, "text", text, "")
                removeFromContentValues(this, "time", time, -1L)
                removeFromContentValues(this, "like_count", likeCount, -1)
                removeFromContentValues(this, "retweet_count", retweetCount, -1)


            }
        }


        @JvmStatic private fun buildDatabase(context: Context): TalonDatabase {
            val userId = AtomicLong(-2L)


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
                     .addCallback(transferActivityData(context, getDatabasePath(context, ActivitySQLiteHelper.DATABASE_NAME)))
                     .addCallback(transferDirectMessageData(context, dmDbPath))
                     .addCallback(transferEmojiData(context, emojiDbPath))
                     .addCallback(transferFavoriteTweetsData(context, favoriteTweetsDbPath))
                     .addCallback(transferFavoriteUserNotificationsData(context, favoriteUserNotificationsDbPath))
                     .addCallback(transferFavoriteUsersData(context, favoriteUsersDbPath))
                     .addCallback(transferFollowersData(context, followersUsersDbPath))
                     .addCallback(transferHashtagData(context, hashtagDbPath))
                     .addCallback(transferHomeTweetsData(context, homeTweetsDbPath))
                     .addCallback(transferInteractionsData(context, interactionsDbPath))
                     .addCallback(transferListData(context, listDbPath))
                     .addCallback(transferMentionsData(context, mentionsDbPath))
                     .addCallback(transferQueuedData(context, queuedDbPath))
                     .addCallback(transferSavedTweetsData(context, savedTweetsDbPath, userId))
                     .addCallback(transferUserTweetsData(context, userTweetsDbPath))
                     .build()
        }

        private fun getDatabasePath(context: Context, relativePath: String): String {
            return context.getDatabasePath(relativePath).absolutePath
        }


        @JvmStatic fun transferActivityData(context: Context, absolutePath: String): RoomDatabase.Callback {
            return object : TransferCallback(absolutePath, context) {
                override fun readDatabase(context: Context, db: SupportSQLiteDatabase, source: SQLiteDatabase) {

                    source.query(ActivitySQLiteHelper.TABLE_ACTIVITY, null, null, null, null, null, null).use { cursor ->

                        if (cursor.moveToFirst()) {

                            do {

                            } while (cursor.moveToNext())

                        }

                    }


                }
            }
        }

        @JvmStatic fun transferDirectMessageData(context: Context, absolutePath: String): RoomDatabase.Callback {
            return object : TransferCallback(absolutePath, context) {
                override fun readDatabase(context: Context, db: SupportSQLiteDatabase, source: SQLiteDatabase) {

                    val dmSize = AppSettings.getInstance(context).dmSize

                    source.query(DMSQLiteHelper.TABLE_DM,
                            null,
                            null,
                            null,
                            DMSQLiteHelper.COLUMN_ID,
                            null,
                            DMSQLiteHelper.COLUMN_TWEET_ID + " DESC",
                            dmSize.toString()).use { cursor ->

                        if (cursor.moveToFirst()) {

                            do {



                            } while (cursor.moveToNext())


                        }

                    }


                }
            }
        }

        @JvmStatic fun transferEmojiData(context: Context, absolutePath: String): RoomDatabase.Callback {
            return object : TransferCallback(absolutePath, context) {
                override fun readDatabase(context: Context, db: SupportSQLiteDatabase, source: SQLiteDatabase) {

                    source.query(EmojiSQLiteHelper.TABLE_RECENTS, null, null, null, null, null, EmojiSQLiteHelper.COLUMN_COUNT + " DESC", "60").use { cursor ->
                        if (cursor.moveToFirst()) {
                            val contentValues = ContentValues()

                            do {
                                val text = cursor.getString(cursor.getColumnIndex(EmojiSQLiteHelper.COLUMN_TEXT))
                                val icon = cursor.getString(cursor.getColumnIndex(EmojiSQLiteHelper.COLUMN_ICON))
                                val count = cursor.getInt(cursor.getColumnIndex(EmojiSQLiteHelper.COLUMN_COUNT))

                                with(contentValues) {
                                    put("text", text)
                                    put("icon", icon)
                                    put("count", count)
                                }


                                db.insert("emojis", SQLiteDatabase.CONFLICT_IGNORE, contentValues)

                            } while (cursor.moveToNext())

                        }
                    }

                }
            }

        }

        @JvmStatic fun transferFavoriteTweetsData(context: Context, absolutePath: String): RoomDatabase.Callback {
            return object : TransferCallback(absolutePath, context) {
                override fun readDatabase(context: Context, db: SupportSQLiteDatabase, source: SQLiteDatabase) {

                    source.query(FavoriteTweetsSQLiteHelper.TABLE_FAVORITE_TWEETS,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            "400").use { cursor ->

                        if (cursor.moveToFirst()) {

                            db.beginTransaction()


                            do {

                            } while (cursor.moveToNext())

                            db.setTransactionSuccessful()
                            db.endTransaction()

                        }



                    }




                }
            }
        }

        @JvmStatic fun transferFavoriteUserNotificationsData(context: Context, absolutePath: String): RoomDatabase.Callback {
            return object : TransferCallback(absolutePath, context) {
                override fun readDatabase(context: Context, db: SupportSQLiteDatabase, source: SQLiteDatabase) {

                    source.query(FavoriteUserNotificationSQLiteHelper.TABLE, arrayOf(FavoriteUserNotificationSQLiteHelper.COLUMN_TWEET_ID), null, null, null, null, null, "100").use { cursor ->

                        if (cursor.moveToFirst()) {

                            val contentValues = ContentValues()

                            db.beginTransaction()

                            do {

                                val tweetId = cursor.getLong(cursor.getColumnIndex(FavoriteUserNotificationSQLiteHelper.COLUMN_TWEET_ID))
                                contentValues.put("tweet_id", tweetId)
                                db.insert("favorite_user_notifications", SQLiteDatabase.CONFLICT_IGNORE, contentValues)

                            } while (cursor.moveToNext())

                            db.setTransactionSuccessful()
                            db.endTransaction()

                        }

                    }

                }
            }
        }


        @JvmStatic fun transferFavoriteUsersData(context: Context, absolutePath: String): RoomDatabase.Callback {
            return object : TransferCallback(absolutePath, context) {
                override fun readDatabase(context: Context, db: SupportSQLiteDatabase, source: SQLiteDatabase) {


                    source.query(FavoriteUsersSQLiteHelper.TABLE_HOME, null, null, null, null, null, null).use { cursor ->

                        if (cursor.moveToFirst()) {


                            do {



                            } while (cursor.moveToNext())


                        }



                    }

                }
            }
        }

        @JvmStatic fun transferFollowersData(context: Context, absolutePath: String): RoomDatabase.Callback {
            return object : TransferCallback(absolutePath, context) {
                override fun readDatabase(context: Context, db: SupportSQLiteDatabase, source: SQLiteDatabase) {

                    source.query(FollowersSQLiteHelper.TABLE_HOME, null, null, null, null, null, FollowersSQLiteHelper.COLUMN_ID + " DESC" ).use { cursor ->

                        if (cursor.moveToFirst()) {

                            val userContentValues = ContentValues()
                            val followerContentValues = ContentValues()


                            db.beginTransaction()

                            do {


                            } while (cursor.moveToNext())

                            db.setTransactionSuccessful()
                            db.endTransaction()

                        }
                    }

                }
            }
        }

        @JvmStatic fun transferHashtagData(context: Context, absolutePath: String): RoomDatabase.Callback {
            return object : TransferCallback(absolutePath, context) {
                override fun readDatabase(context: Context, db: SupportSQLiteDatabase, source: SQLiteDatabase) {

                    source.query(HashtagSQLiteHelper.TABLE_HASHTAGS, arrayOf(HashtagSQLiteHelper.COLUMN_TAG), null, null, null, null, null, "500").use { cursor ->




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

        @JvmStatic fun transferHomeTweetsData(context: Context, absolutePath: String): RoomDatabase.Callback {
            return object : TransferCallback(absolutePath, context) {
                override fun readDatabase(context: Context, db: SupportSQLiteDatabase, source: SQLiteDatabase) {

                    source.query(HomeSQLiteHelper.TABLE_HOME, null, null, null, null, null, null).use { cursor ->

                        if (cursor.moveToFirst()) {

                            val userContentValues = ContentValues()
                            val tweetContentValues = ContentValues()
                            val homeTweetContentValues = ContentValues()

                            db.beginTransaction()

                            do {



                            } while (cursor.moveToNext())

                            db.setTransactionSuccessful()
                            db.endTransaction()

                        }

                    }

                }
            }
        }


        @JvmStatic fun transferInteractionsData(context: Context, absolutePath: String): RoomDatabase.Callback {
            return object : TransferCallback(absolutePath, context) {
                override fun readDatabase(context: Context, db: SupportSQLiteDatabase, source: SQLiteDatabase) {

                    source.query(InteractionsSQLiteHelper.TABLE_INTERACTIONS, null, null, null, null, null, null).use { cursor ->

                        if (cursor.moveToFirst()) {

                            do {



                            } while (cursor.moveToNext())

                        }


                    }

                }
            }
        }


        @JvmStatic fun transferListData(context: Context, absolutePath: String): RoomDatabase.Callback {
            return object : TransferCallback(absolutePath, context) {
                override fun readDatabase(context: Context, db: SupportSQLiteDatabase, source: SQLiteDatabase) {

                    val listSize = AppSettings.getInstance(context).listSize


                    source.query(ListSQLiteHelper.TABLE_HOME,
                            null,
                            null,
                            null,
                            null,
                            null,
                            ListSQLiteHelper.COLUMN_ID + " DESC",
                            listSize.toString()).use { cursor ->

                        if (cursor.moveToFirst()) {


                            do {


                            } while (cursor.moveToNext())


                        }

                    }

                }
            }
        }


        @JvmStatic fun transferMentionsData(context: Context, absolutePath: String): RoomDatabase.Callback {
            return object : TransferCallback(absolutePath, context) {
                override fun readDatabase(context: Context, db: SupportSQLiteDatabase, source: SQLiteDatabase) {

                    source.query(MentionsSQLiteHelper.TABLE_MENTIONS, null, null, null, null, null, null).use { cursor ->

                        if (cursor.moveToFirst()) {

                            do {


                            } while (cursor.moveToNext())

                        }


                    }



                }
            }
        }


        @JvmStatic fun transferQueuedData(context: Context, absolutePath: String): RoomDatabase.Callback {
            return object : TransferCallback(absolutePath, context) {
                override fun readDatabase(context: Context, db: SupportSQLiteDatabase, source: SQLiteDatabase) {

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


        @JvmStatic fun transferSavedTweetsData(context: Context, absolutePath: String, userLabeler: AtomicLong): RoomDatabase.Callback {
            return object : TransferCallback(absolutePath, context) {
                override fun readDatabase(context: Context, db: SupportSQLiteDatabase, source: SQLiteDatabase) {

                    source.query(SavedTweetSQLiteHelper.TABLE_HOME, null, null, null, null, null, null).use { cursor ->

                        if (cursor.moveToFirst()) {



                            do {



                            } while (cursor.moveToNext())

                        }

                    }

                }
            }
        }


        @JvmStatic fun transferUserTweetsData(context: Context, absolutePath: String): RoomDatabase.Callback {
            return object : TransferCallback(absolutePath, context) {
                override fun readDatabase(context: Context, db: SupportSQLiteDatabase, source: SQLiteDatabase) {

                    val userTweetsSize = AppSettings.getInstance(context).userTweetsSize

                    source.query(UserTweetsSQLiteHelper.TABLE_HOME, null, null, null, null, null, null, userTweetsSize.toString()).use { cursor ->

                        if (cursor.moveToFirst()) {

                            do {




                            } while (cursor.moveToNext())



                        }

                    }

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
