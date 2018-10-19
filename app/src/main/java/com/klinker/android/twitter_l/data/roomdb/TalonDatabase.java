package com.klinker.android.twitter_l.data.roomdb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.strictmode.SqliteObjectLeakedViolation;

import com.klinker.android.twitter_l.data.sq_lite.DMSQLiteHelper;
import com.klinker.android.twitter_l.data.sq_lite.FavoriteUsersSQLiteHelper;
import com.klinker.android.twitter_l.data.sq_lite.HashtagSQLiteHelper;
import com.klinker.android.twitter_l.data.sq_lite.InteractionsSQLiteHelper;
import com.klinker.android.twitter_l.data.sq_lite.QueuedSQLiteHelper;

import java.io.File;
import java.sql.SQLInput;
import java.util.Queue;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
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

    public static TalonDatabase getInstance(Context context) {

        if (dbInstance == null) {
            dbInstance = Room.databaseBuilder(context.getApplicationContext(), TalonDatabase.class, "talondb" )
                    .addCallback(transferOldDatabaseData(context))
                    .build();
        }

        return dbInstance;

    }


    private static Callback transferOldDatabaseData(Context context) {

        return new Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);

                handleActivityTransfer(context, db);
                handleDirectMessagesTransfer(context, db);
                handleEmojiTransfer(context, db);
                handleFavoriteTweetsTransfer(context, db);
                handleFavoriteUserNotificationsTransfer(context, db);
                handleFavoriteUserTransfer(context, db);
                handleFollowersTransfer(context, db);
                handleHashtagsTransfer(context, db);
                handleHomeTweetsTransfer(context, db);
                handleInteractionsTransfer(context, db);
                handleListTransfer(context, db);
                handleMentionsTransfer(context, db);
                handleQueuedTransfer(context, db);
                handleSavedTweetsTransfer(context, db);
                handleUserTweetsTransfer(context, db);

                //delete databases
            }

            private long userId = -2;

            private SQLiteDatabase getDatabase(Context context, String relativeDbFilename) {
                File dbPath = context.getDatabasePath(relativeDbFilename);
                try {
                    return SQLiteDatabase.openDatabase(dbPath.getAbsolutePath(), null, SQLiteDatabase.OPEN_READONLY);
                } catch (Exception ignored) {
                    return null;
                }
            }

            private Cursor getDatabaseTable(SQLiteDatabase db, String tableName) {
                if (db == null) {
                    return null;
                } else {
                    return db.query(tableName, null, null, null, null, null, null);
                }
            }

            private void handleActivityTransfer(Context context, SupportSQLiteDatabase db) {

            }

            private void handleEmojiTransfer(Context context, SupportSQLiteDatabase db) {

            }


            private void handleFavoriteUserNotificationsTransfer(Context context, SupportSQLiteDatabase db) {

            }

            private void handleFollowersTransfer(Context context, SupportSQLiteDatabase db) {

            }


            private void handleHashtagsTransfer(Context context, SupportSQLiteDatabase db) {

                try (
                        SQLiteDatabase hashtagsDb = getDatabase(context, "hashtags.db");
                        Cursor cursor = getDatabaseTable(hashtagsDb, HashtagSQLiteHelper.TABLE_HASHTAGS)
                ) {

                    if (hashtagsDb != null && cursor != null && cursor.moveToFirst()) {
                        ContentValues contentValues = new ContentValues();

                        do {

                            String hashtag = cursor.getString(cursor.getColumnIndex(HashtagSQLiteHelper.COLUMN_TAG));
                            contentValues.put("name", hashtag);
                            db.insert("hashtags", SQLiteDatabase.CONFLICT_IGNORE, contentValues);

                        } while (cursor.moveToNext());

                    }
                }
            }

            private void handleHomeTweetsTransfer(Context context, SupportSQLiteDatabase db) {

            }

            private void handleListTransfer(Context context, SupportSQLiteDatabase db) {

            }

            private void handleMentionsTransfer(Context context, SupportSQLiteDatabase db) {

            }

            private void handleQueuedTransfer(Context context, SupportSQLiteDatabase db) {


                try (
                        SQLiteDatabase queuedDb = getDatabase(context, "queued.db");
                        Cursor cursor = getDatabaseTable(queuedDb, QueuedSQLiteHelper.TABLE_QUEUED)
                ) {

                    if (queuedDb != null && cursor != null && cursor.moveToFirst()) {
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

                }

            }

            private void handleDirectMessagesTransfer(Context context, SupportSQLiteDatabase db) {

                try (
                        SQLiteDatabase dmDb = getDatabase(context, "direct_messages.db");
                        Cursor cursor = getDatabaseTable(dmDb, DMSQLiteHelper.TABLE_DM);
                ) {

                    if (dmDb != null && cursor != null && cursor.moveToFirst()) {

                        ContentValues contentValues = new ContentValues();

                        do {


                            contentValues.clear();
                        } while (cursor.moveToNext());

                    }


                }

            }


            private void handleFavoriteTweetsTransfer(Context context, SupportSQLiteDatabase db) {


            }


            private void handleFavoriteUserTransfer(Context context, SupportSQLiteDatabase db) {

                try (
                        SQLiteDatabase favoriteUsersDb = getDatabase(context, "favUsers.db");
                        Cursor cursor = getDatabaseTable(favoriteUsersDb, FavoriteUsersSQLiteHelper.TABLE_HOME)
                ) {

                    if (favoriteUsersDb != null && cursor != null && cursor.moveToFirst()) {
                        ContentValues userContentValues = new ContentValues();
                        ContentValues favoriteUserContentValues = new ContentValues();

                        do {

                            String name = cursor.getString(cursor.getColumnIndex(FavoriteUsersSQLiteHelper.COLUMN_NAME));
                            String screenName = cursor.getString(cursor.getColumnIndex(FavoriteUsersSQLiteHelper.COLUMN_SCREEN_NAME));
                            String proPic = cursor.getString(cursor.getColumnIndex(FavoriteUsersSQLiteHelper.COLUMN_PRO_PIC));
                            int account = cursor.getInt(cursor.getColumnIndex(FavoriteUsersSQLiteHelper.COLUMN_ACCOUNT));

                            userContentValues.put("id", userId);
                            userContentValues.put("name", name);
                            userContentValues.put("screen_name", screenName);
                            userContentValues.put("profile_pic", proPic);

                            favoriteUserContentValues.put("account", account);

                            try {
                                db.beginTransaction();
                                long id = db.insert("users", SQLiteDatabase.CONFLICT_IGNORE, userContentValues);
                                if (id != -1) {
                                    db.insert("favorite_users", SQLiteDatabase.CONFLICT_IGNORE, favoriteUserContentValues);
                                }
                                db.setTransactionSuccessful();
                                userId--;

                            } finally {
                                db.endTransaction();
                            }

                        } while (cursor.moveToNext());
                    }
                }
            }

            private void handleInteractionsTransfer(Context context, SupportSQLiteDatabase db) {

            }

            private void handleSavedTweetsTransfer(Context context, SupportSQLiteDatabase db) {

            }

            private void handleUserTweetsTransfer(Context context, SupportSQLiteDatabase db) {

            }


        };
    }




    public static void destroyInstance() {
        dbInstance = null;
    }



}
