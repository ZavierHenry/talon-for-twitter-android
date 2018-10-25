package com.klinker.android.twitter_l.data.roomdb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.RemoteCallbackList;
import android.os.strictmode.SqliteObjectLeakedViolation;
import android.telecom.Call;

import com.klinker.android.twitter_l.data.roomdb.daos.ActivityDao;
import com.klinker.android.twitter_l.data.roomdb.daos.DirectMessageDao;
import com.klinker.android.twitter_l.data.roomdb.daos.DraftDao;
import com.klinker.android.twitter_l.data.roomdb.daos.EmojiDao;
import com.klinker.android.twitter_l.data.roomdb.daos.FavoriteTweetDao;
import com.klinker.android.twitter_l.data.roomdb.daos.FavoriteUserDao;
import com.klinker.android.twitter_l.data.roomdb.daos.FavoriteUserNotificationDao;
import com.klinker.android.twitter_l.data.roomdb.daos.FollowerDao;
import com.klinker.android.twitter_l.data.roomdb.daos.HashtagDao;
import com.klinker.android.twitter_l.data.roomdb.daos.HomeTweetDao;
import com.klinker.android.twitter_l.data.roomdb.daos.InteractionDao;
import com.klinker.android.twitter_l.data.roomdb.daos.ListTweetDao;
import com.klinker.android.twitter_l.data.roomdb.daos.MentionDao;
import com.klinker.android.twitter_l.data.roomdb.daos.QueuedTweetDao;
import com.klinker.android.twitter_l.data.roomdb.daos.SavedTweetDao;
import com.klinker.android.twitter_l.data.roomdb.daos.ScheduledTweetDao;
import com.klinker.android.twitter_l.data.roomdb.daos.UserTweetDao;
import com.klinker.android.twitter_l.data.roomdb.entities.Activity;
import com.klinker.android.twitter_l.data.roomdb.entities.DirectMessage;
import com.klinker.android.twitter_l.data.roomdb.entities.Draft;
import com.klinker.android.twitter_l.data.roomdb.entities.Emoji;
import com.klinker.android.twitter_l.data.roomdb.entities.FavoriteTweet;
import com.klinker.android.twitter_l.data.roomdb.entities.FavoriteUser;
import com.klinker.android.twitter_l.data.roomdb.entities.FavoriteUserNotification;
import com.klinker.android.twitter_l.data.roomdb.entities.Follower;
import com.klinker.android.twitter_l.data.roomdb.entities.Hashtag;
import com.klinker.android.twitter_l.data.roomdb.entities.HomeTweet;
import com.klinker.android.twitter_l.data.roomdb.entities.Interaction;
import com.klinker.android.twitter_l.data.roomdb.entities.ListTweet;
import com.klinker.android.twitter_l.data.roomdb.entities.Mention;
import com.klinker.android.twitter_l.data.roomdb.entities.QueuedTweet;
import com.klinker.android.twitter_l.data.roomdb.entities.SavedTweet;
import com.klinker.android.twitter_l.data.roomdb.entities.ScheduledTweet;
import com.klinker.android.twitter_l.data.roomdb.entities.Tweet;
import com.klinker.android.twitter_l.data.roomdb.entities.User;
import com.klinker.android.twitter_l.data.roomdb.entities.UserTweet;
import com.klinker.android.twitter_l.data.sq_lite.ActivitySQLiteHelper;
import com.klinker.android.twitter_l.data.sq_lite.DMSQLiteHelper;
import com.klinker.android.twitter_l.data.sq_lite.EmojiSQLiteHelper;
import com.klinker.android.twitter_l.data.sq_lite.FavoriteTweetsSQLiteHelper;
import com.klinker.android.twitter_l.data.sq_lite.FavoriteUserNotificationSQLiteHelper;
import com.klinker.android.twitter_l.data.sq_lite.FavoriteUsersSQLiteHelper;
import com.klinker.android.twitter_l.data.sq_lite.FollowersSQLiteHelper;
import com.klinker.android.twitter_l.data.sq_lite.HashtagSQLiteHelper;
import com.klinker.android.twitter_l.data.sq_lite.HomeSQLiteHelper;
import com.klinker.android.twitter_l.data.sq_lite.InteractionsSQLiteHelper;
import com.klinker.android.twitter_l.data.sq_lite.ListSQLiteHelper;
import com.klinker.android.twitter_l.data.sq_lite.MentionsSQLiteHelper;
import com.klinker.android.twitter_l.data.sq_lite.QueuedSQLiteHelper;
import com.klinker.android.twitter_l.data.sq_lite.SavedTweetSQLiteHelper;
import com.klinker.android.twitter_l.data.sq_lite.UserTweetsSQLiteHelper;

import java.io.File;
import java.sql.SQLInput;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicLong;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {
        Activity.class,
        DirectMessage.class,
        Draft.class,
        Emoji.class,
        FavoriteTweet.class,
        FavoriteUser.class,
        FavoriteUserNotification.class,
        Follower.class,
        Hashtag.class,
        HomeTweet.class,
        Interaction.class,
        ListTweet.class,
        Mention.class,
        QueuedTweet.class,
        SavedTweet.class,
        ScheduledTweet.class,
        Tweet.class,
        User.class,
        UserTweet.class }, version = 1, exportSchema = false)
public abstract class TalonDatabase extends RoomDatabase {

    private static TalonDatabase dbInstance = null;

    public abstract ActivityDao activityDao();
    public abstract DirectMessageDao directMessageDao();
    public abstract DraftDao draftDao();
    public abstract EmojiDao emojiDao();
    public abstract FavoriteTweetDao favoriteTweetDao();
    public abstract FavoriteUserDao favoriteUserDao();
    public abstract FavoriteUserNotificationDao favoriteUserNotificationDao();
    public abstract FollowerDao followerDao();
    public abstract HashtagDao hashtagDao();
    public abstract HomeTweetDao homeTweetDao();
    public abstract InteractionDao interactionDao();
    public abstract ListTweetDao listTweetDao();
    public abstract MentionDao mentionDao();
    public abstract QueuedTweetDao queuedTweetDao();
    public abstract SavedTweetDao savedTweetDao();
    public abstract ScheduledTweetDao scheduledTweetDao();
    public abstract UserTweetDao userTweetDao();

    //list table name

    //list table columns

    public static TalonDatabase getInstance(@NonNull Context context) {

        if (dbInstance == null) {
            AtomicLong userId = new AtomicLong(-2L);

            String activityDbPath = context.getDatabasePath(ActivitySQLiteHelper.DATABASE_NAME).getAbsolutePath();
            String dmDbPath = context.getDatabasePath(DMSQLiteHelper.DATABASE_NAME).getAbsolutePath();
            String emojiDbPath = context.getDatabasePath(EmojiSQLiteHelper.DATABASE_NAME).getAbsolutePath();
            String favoriteTweetsDbPath = context.getDatabasePath(FavoriteTweetsSQLiteHelper.DATABASE_NAME).getAbsolutePath();
            String favoriteUserNotificationsDbPath = context.getDatabasePath(FavoriteUserNotificationSQLiteHelper.DATABASE_NAME).getAbsolutePath();
            String favoriteUsersDbPath = context.getDatabasePath(FavoriteUsersSQLiteHelper.DATABASE_NAME).getAbsolutePath();
            String followersUsersDbPath = context.getDatabasePath(FollowersSQLiteHelper.DATABASE_NAME).getAbsolutePath();
            String hashtagDbPath = context.getDatabasePath(HashtagSQLiteHelper.DATABASE_NAME).getAbsolutePath();
            String homeTweetsDbPath = context.getDatabasePath(HomeSQLiteHelper.DATABASE_NAME).getAbsolutePath();
            String interactionsDbPath = context.getDatabasePath(InteractionsSQLiteHelper.DATABASE_NAME).getAbsolutePath();
            String listDbPath = context.getDatabasePath(ListSQLiteHelper.DATABASE_NAME).getAbsolutePath();
            String mentionsDbPath = context.getDatabasePath(MentionsSQLiteHelper.DATABASE_NAME).getAbsolutePath();
            String queuedDbPath = context.getDatabasePath(QueuedSQLiteHelper.DATABASE_NAME).getAbsolutePath();
            String savedTweetsDbPath = context.getDatabasePath(SavedTweetSQLiteHelper.DATABASE_NAME).getAbsolutePath();
            String userTweetsDbPath = context.getDatabasePath(UserTweetsSQLiteHelper.DATABASE_NAME).getAbsolutePath();


            dbInstance = Room.databaseBuilder(context.getApplicationContext(), TalonDatabase.class, "talondata.db" )
                    .addCallback(transferActivityData(activityDbPath, userId))
                    .addCallback(transferDirectMessageData(dmDbPath, userId))
                    .addCallback(transferEmojiData(emojiDbPath))
                    .addCallback(transferFavoriteTweetsData(favoriteTweetsDbPath))
                    .addCallback(transferFavoriteUserNotificationsData(favoriteUserNotificationsDbPath))
                    .addCallback(transferFavoriteUsersData(favoriteUsersDbPath))
                    .addCallback(transferFollowersData(followersUsersDbPath))
                    .addCallback(transferHashtagData(hashtagDbPath))
                    .addCallback(transferHomeTweetsData(homeTweetsDbPath))
                    .addCallback(transferInteractionsData(interactionsDbPath))
                    .addCallback(transferListData(listDbPath))
                    .addCallback(transferMentionsData(mentionsDbPath))
                    .addCallback(transferQueuedData(queuedDbPath))
                    .addCallback(transferSavedTweetsData(savedTweetsDbPath))
                    .addCallback(transferUserTweetsData(userTweetsDbPath))
                    .build();

            //delete old databases if necessary

        }

        return dbInstance;

    }

    //delete databases
    //TODO: rewrite callbacks to facilitate testing

    private abstract static class TransferCallback extends Callback {

        private String tableName;
        private String databasePath;

        abstract void readDatabaseRow(SupportSQLiteDatabase db, Cursor cursor);

        private TransferCallback(String absolutePath, String tableName) {
            this.tableName = tableName;
            this.databasePath = databasePath;
        }


        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            try (
                    SQLiteDatabase database = getDatabase(databasePath);
                    Cursor cursor = getDatabaseTable(database, tableName)
            ) {

                if (cursor != null && cursor.moveToFirst()) {
                    readDatabaseRow(db, cursor);
                }
            }
        }

        private SQLiteDatabase getDatabase(String absolutePath) {

            try {
                return SQLiteDatabase.openDatabase(absolutePath, null, SQLiteDatabase.OPEN_READONLY);
            } catch (Exception e) {
                return null;
            }
        }

        private Cursor getDatabaseTable(SQLiteDatabase db, String tableName) {

            if (db == null) {
                return null;
            }

            try {
                return db.query(tableName, null, null, null, null, null, null);
            } catch (Exception e) {
                return null;
            }

        }

    }

    private static SQLiteDatabase getDatabase(Context context, String relativePath) {
        if (context == null) {
            return null;
        }

        File dbPath = context.getDatabasePath(relativePath);
        if (!dbPath.exists()) {
            return null;
        }

        try {
            return SQLiteDatabase.openDatabase(dbPath.getAbsolutePath(), null, SQLiteDatabase.OPEN_READONLY);
        } catch (Exception e) {
            return null;
        }
    }

    private static SQLiteDatabase getDatabase(String absolutePath) {

        try {
            return SQLiteDatabase.openDatabase(absolutePath, null, SQLiteDatabase.OPEN_READONLY);
        } catch (Exception e) {
            return null;
        }

    }



    public static Callback transferActivityData(String absolutePath, AtomicLong userIdLabeler) {
        return new TransferCallback(absolutePath, ActivitySQLiteHelper.TABLE_ACTIVITY) {
            @Override
            void readDatabaseRow(SupportSQLiteDatabase db, Cursor cursor) {

            }
        };
    }


    public static Callback transferDirectMessageData(String absolutePath, AtomicLong userIdLabeler) {
        return new TransferCallback(absolutePath, DMSQLiteHelper.TABLE_DM) {
            @Override
            void readDatabaseRow(SupportSQLiteDatabase db, Cursor cursor) {

            }
        };
    }


    public static Callback transferEmojiData(String absolutePath) {
        return new TransferCallback(absolutePath, EmojiSQLiteHelper.TABLE_RECENTS) {
            @Override
            void readDatabaseRow(SupportSQLiteDatabase db, Cursor cursor) {

                ContentValues contentValues = new ContentValues();

                do {

                    String text = cursor.getString(cursor.getColumnIndex(EmojiSQLiteHelper.COLUMN_TEXT));
                    String icon = cursor.getString(cursor.getColumnIndex(EmojiSQLiteHelper.COLUMN_ICON));
                    int count = cursor.getInt(cursor.getColumnIndex(EmojiSQLiteHelper.COLUMN_COUNT));

                    contentValues.put("text", text);
                    contentValues.put("icon", icon);
                    contentValues.put("count", count);

                    db.insert("emojis", SQLiteDatabase.CONFLICT_IGNORE, contentValues);

                } while (cursor.moveToNext());

            }
        };
    }

    public static Callback transferFavoriteTweetsData(String absolutePath) {
        return new TransferCallback(absolutePath, FavoriteTweetsSQLiteHelper.TABLE_FAVORITE_TWEETS) {
            @Override
            void readDatabaseRow(SupportSQLiteDatabase db, Cursor cursor) {
                ContentValues userContentValues = new ContentValues();
                ContentValues tweetContentValues = new ContentValues();
                ContentValues favoriteTweetContentValues = new ContentValues();

                do {



                } while (cursor.moveToNext());

            }
        };
    }

    public static Callback transferFavoriteUserNotificationsData(String absolutePath) {
        return new TransferCallback(absolutePath, FavoriteUserNotificationSQLiteHelper.TABLE) {
            @Override
            void readDatabaseRow(SupportSQLiteDatabase db, Cursor cursor) {

            }
        };
    }


    public static Callback transferFavoriteUsersData(String absolutePath) {
        return new TransferCallback(absolutePath, FavoriteUsersSQLiteHelper.TABLE_HOME) {
            @Override
            void readDatabaseRow(SupportSQLiteDatabase db, Cursor cursor) {
                ContentValues userValues = new ContentValues();
                ContentValues favoriteUsersValues = new ContentValues();

                do {

                    long id = cursor.getLong(cursor.getColumnIndex(FavoriteUsersSQLiteHelper.COLUMN_ID));
                    int account = cursor.getInt(cursor.getColumnIndex(FavoriteUsersSQLiteHelper.COLUMN_ACCOUNT));
                    String name = cursor.getString(cursor.getColumnIndex(FavoriteUsersSQLiteHelper.COLUMN_NAME));
                    String screenName = cursor.getString(cursor.getColumnIndex(FavoriteUsersSQLiteHelper.COLUMN_SCREEN_NAME));
                    String profilePic = cursor.getString(cursor.getColumnIndex(FavoriteUsersSQLiteHelper.COLUMN_PRO_PIC));


                    favoriteUsersValues.put("account", account);
                    favoriteUsersValues.put("user_id", id);

                    userValues.put("id", id);
                    userValues.put("name", name);
                    userValues.put("screen_name", screenName);
                    userValues.put("profile_pic", profilePic);
                    userValues.put("is_verified", false);

                    //insert in user, replace user if screen_name already in database

                    db.insert("favorite_tweets", SQLiteDatabase.CONFLICT_IGNORE, favoriteUsersValues);

                } while (cursor.moveToNext());


            }
        };
    }


    public static Callback transferFollowersData(String absolutePath) {
        return new TransferCallback(absolutePath, FollowersSQLiteHelper.TABLE_HOME) {
            @Override
            void readDatabaseRow(SupportSQLiteDatabase db, Cursor cursor) {

            }
        };
    }

    public static Callback transferHashtagData(String absolutePath) {
        return new TransferCallback(absolutePath, HashtagSQLiteHelper.TABLE_HASHTAGS) {
            @Override
            public void readDatabaseRow(SupportSQLiteDatabase db, Cursor cursor) {
                ContentValues contentValues = new ContentValues();

                do {

                    String name = cursor.getString(cursor.getColumnIndex(HashtagSQLiteHelper.COLUMN_TAG));
                    contentValues.put("name", name);
                    db.insert("hashtags", SQLiteDatabase.CONFLICT_IGNORE, contentValues);

                } while (cursor.moveToNext());

            }
        };
    }

    public static Callback transferHomeTweetsData(String absolutePath) {
        return new TransferCallback(absolutePath, HomeSQLiteHelper.TABLE_HOME) {
            @Override
            void readDatabaseRow(SupportSQLiteDatabase db, Cursor cursor) {

            }
        };
    }


    public static Callback transferInteractionsData(String absolutePath) {
        return new TransferCallback(absolutePath, InteractionsSQLiteHelper.TABLE_INTERACTIONS) {
            @Override
            void readDatabaseRow(SupportSQLiteDatabase db, Cursor cursor) {

            }
        };
    }


    public static Callback transferListData(String absolutePath) {
        return new TransferCallback(absolutePath, ListSQLiteHelper.TABLE_HOME) {
            @Override
            void readDatabaseRow(SupportSQLiteDatabase db, Cursor cursor) {

            }
        };
    }


    public static Callback transferMentionsData(String absolutePath) {
        return new TransferCallback(absolutePath, MentionsSQLiteHelper.TABLE_MENTIONS) {
            @Override
            void readDatabaseRow(SupportSQLiteDatabase db, Cursor cursor) {

            }
        };
    }


    public static Callback transferQueuedData(String absolutePath) {
        return new TransferCallback(absolutePath, QueuedSQLiteHelper.TABLE_QUEUED) {
            @Override
            void readDatabaseRow(SupportSQLiteDatabase db, Cursor cursor) {

                ContentValues contentValues = new ContentValues();

                do {

                    int type = cursor.getInt(cursor.getColumnIndex(QueuedSQLiteHelper.COLUMN_TYPE));
                    int account = cursor.getInt(cursor.getColumnIndex(QueuedSQLiteHelper.COLUMN_ACCOUNT));
                    String text = cursor.getString(cursor.getColumnIndex(QueuedSQLiteHelper.COLUMN_TEXT));
                    if (text == null) { text = ""; }

                    switch (type) {
                        case QueuedSQLiteHelper.TYPE_SCHEDULED:
                            int alarmId = cursor.getInt(cursor.getColumnIndex(QueuedSQLiteHelper.COLUMN_ALARM_ID));
                            contentValues.put("alarm_id", alarmId);
                            contentValues.put("account", account);
                            contentValues.put("text", text);
                            db.insert("scheduled_tweets", SQLiteDatabase.CONFLICT_IGNORE, contentValues);
                            break;

                        case QueuedSQLiteHelper.TYPE_DRAFT:
                            contentValues.put("account", account);
                            contentValues.put("text", text);
                            db.insert("drafts", SQLiteDatabase.CONFLICT_IGNORE, contentValues);
                            break;

                        case QueuedSQLiteHelper.TYPE_QUEUED_TWEET:
                            contentValues.put("account", account);
                            contentValues.put("text", text);
                            db.insert("queued_tweets", SQLiteDatabase.CONFLICT_IGNORE, contentValues);
                            break;
                    }

                    contentValues.clear();

                } while (cursor.moveToNext());

            }
        };
    }


    public static Callback transferSavedTweetsData(String absolutePath) {
        return new TransferCallback(absolutePath, SavedTweetSQLiteHelper.TABLE_HOME) {
            @Override
            void readDatabaseRow(SupportSQLiteDatabase db, Cursor cursor) {

            }
        };
    }


    public static Callback transferUserTweetsData(String absolutePath) {
        return new TransferCallback(absolutePath, UserTweetsSQLiteHelper.TABLE_HOME) {
            @Override
            void readDatabaseRow(SupportSQLiteDatabase db, Cursor cursor) {
                ContentValues tweetValues = new ContentValues();
                ContentValues userValues = new ContentValues();
                ContentValues userTweetValues = new ContentValues();

                do {

                    //fill content values

                    db.insert("user_tweets", SQLiteDatabase.CONFLICT_IGNORE, userTweetValues);

                } while (cursor.moveToNext());
            }
        };
    }



    public static void destroyInstance() {
        dbInstance = null;
    }



}
