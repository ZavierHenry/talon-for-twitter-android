package com.klinker.android.twitter_l.data.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Draft::class], version = 1)
abstract class TalonDatabase : RoomDatabase() {

    companion object {
        var database: TalonDatabase? = null

        fun getInstance(context: Context) : TalonDatabase {
            if (database == null) {
                database = Room.databaseBuilder(context, TalonDatabase::class.java, "talon-database").build()
            }
            return database!!
        }
    }
}