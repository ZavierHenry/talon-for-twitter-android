package com.klinker.android.twitter_l.data.roomdb.transfers

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.klinker.android.twitter_l.data.roomdb.ListStringConverter
import java.io.File

abstract class TalonDatabaseCallback(private val oldDatabaseFile: File, private val tableName: String, private val primaryKey: String? = null) : RoomDatabase.Callback() {

    abstract fun onEachTableRow(cursor: Cursor, newDatabase: SupportSQLiteDatabase)

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)

        val oldDatabase = SQLiteDatabase.openDatabase(oldDatabaseFile.path, null, SQLiteDatabase.OPEN_READONLY)
        oldDatabase.query(tableName, null, null, null, null, null, primaryKey?.let { "$it ASC" }).use { cursor ->
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                onEachTableRow(cursor, db)
                cursor.moveToLast()
            }
        }
    }

    fun reserializeListString(list: String, oldDelimiter: String) : String {
        return list.split(oldDelimiter).let { ListStringConverter().fromListString(it) }
    }

}