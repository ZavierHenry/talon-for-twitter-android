package com.klinker.android.twitter_l.transfertests

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.room.OnConflictStrategy
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.klinker.android.twitter_l.data.roomdb.TalonDatabase
import com.klinker.android.twitter_l.data.roomdb.transfers.TalonDatabaseCallback
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement


class TestTransferDatabase(private val callback: TalonDatabaseCallback, private val tableName: String, columns: List<String>) : TestRule {

    private val sourceDatabase = SQLiteDatabase.create(null)
    private lateinit var destDatabase: TalonDatabase

    init {
        val createTable = columns.joinToString(
                prefix = "create table $tableName (",
                separator = ", ",
                postfix = ");"
        )
        sourceDatabase.execSQL(createTable)
    }

    fun insertIntoSQLiteDatabase(contentValues: ContentValues) : Long? {
        val id = sourceDatabase.insert(tableName, null, contentValues)
        return if (id == -1L) null else id
    }

    fun buildRoomDatabase() {
        destDatabase = with(ApplicationProvider.getApplicationContext<Context>()) {
            Room.inMemoryDatabaseBuilder(this, TalonDatabase::class.java)
                    .addCallback(callback)
                    .build()
        }
    }

    fun queryFromTalonDatabase(query: String, args: Array<Any>) : Cursor {
        return destDatabase.query(query, args)
    }


    override fun apply(base: Statement?, description: Description?): Statement {
        return object : Statement() {
            override fun evaluate() {
                try {
                    base?.evaluate()
                } finally {
                    destDatabase.close()
                    sourceDatabase.close()
                }
            }
        }

    }



}