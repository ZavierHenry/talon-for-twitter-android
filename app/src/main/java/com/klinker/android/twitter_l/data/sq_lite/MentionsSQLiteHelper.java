package com.klinker.android.twitter_l.data.sq_lite;
/*
 * Copyright 2014 Luke Klinker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MentionsSQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_MENTIONS = "mentions";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TWEET_ID = "tweet_id";
    public static final String COLUMN_UNREAD = "unread";
    public static final String COLUMN_ACCOUNT = "account";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_TEXT = "text";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PRO_PIC = "profile_pic";
    public static final String COLUMN_SCREEN_NAME = "screen_name";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_URL = "other_url";
    public static final String COLUMN_PIC_URL = "pic_url";
    public static final String COLUMN_RETWEETER = "retweeter";
    public static final String COLUMN_HASHTAGS = "hashtags";
    public static final String COLUMN_USERS = "users";
    public static final String COLUMN_ANIMATED_GIF = "extra_one";
    public static final String COLUMN_EXTRA_TWO = "extra_two";
    public static final String COLUMN_EXTRA_THREE = "extra_three";
    public static final String COLUMN_CONVERSATION = "conversation";
    public static final String COLUMN_MEDIA_LENGTH = "media_length";

    public static final String DATABASE_NAME = "mentions.db";
    private static final int DATABASE_VERSION = 3;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_MENTIONS + "(" + COLUMN_ID
            + " integer primary key, " + COLUMN_TWEET_ID
            + " integer tweet id, " + COLUMN_UNREAD
            + " integer unread, " + COLUMN_ACCOUNT
            + " integer account num, " + COLUMN_TYPE
            + " integer type of tweet, " + COLUMN_TEXT
            + " text not null, " + COLUMN_NAME
            + " text users name, " + COLUMN_PRO_PIC
            + " text url of pic, " + COLUMN_SCREEN_NAME
            + " text user screen, " + COLUMN_TIME
            + " integer time, " + COLUMN_URL
            + " text other url, " + COLUMN_PIC_URL
            + " text pic url, " + COLUMN_HASHTAGS
            + " text hashtags, " + COLUMN_USERS
            + " text users, " + COLUMN_RETWEETER
            + " text original name, " + COLUMN_ANIMATED_GIF
            + " text extra one, " + COLUMN_EXTRA_TWO
            + " text extra two, " + COLUMN_EXTRA_THREE
            + " text extra three);";

    private static final String DATABASE_ADD_CONVO_FIELD =
            "ALTER TABLE " + TABLE_MENTIONS + " ADD COLUMN " + COLUMN_CONVERSATION + " INTEGER DEFAULT 0";

    private static final String DATABASE_ADD_MEDIA_LENGTH_FIELD =
            "ALTER TABLE " + TABLE_MENTIONS + " ADD COLUMN " + COLUMN_MEDIA_LENGTH + " INTEGER DEFAULT -1";

    public MentionsSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
        database.execSQL(DATABASE_ADD_CONVO_FIELD);
        database.execSQL(DATABASE_ADD_MEDIA_LENGTH_FIELD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL(DATABASE_ADD_CONVO_FIELD);
        }

        if (oldVersion < 3) {
            db.execSQL(DATABASE_ADD_MEDIA_LENGTH_FIELD);
        }
    }

}