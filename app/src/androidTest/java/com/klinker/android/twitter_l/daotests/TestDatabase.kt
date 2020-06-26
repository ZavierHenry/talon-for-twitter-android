package com.klinker.android.twitter_l.daotests

import android.content.Context
import android.database.Cursor
import androidx.room.OnConflictStrategy
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.klinker.android.twitter_l.mockentities.MockEntity
import com.klinker.android.twitter_l.data.roomdb.TalonDatabase
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class TestDatabase(private val tableName: String) : TestRule {

    val database = with(ApplicationProvider.getApplicationContext<Context>()) {
        Room.inMemoryDatabaseBuilder(this, TalonDatabase::class.java).build()
    }

    val size: Int
        get() = database.query("SELECT COUNT(1) FROM $tableName", emptyArray()).use { cursor ->
                    cursor.moveToFirst()
                    cursor.getInt(0)
                }

    fun insertIntoDatabase(entity: MockEntity) : Long {
        return database.openHelper.writableDatabase.insert(tableName, OnConflictStrategy.IGNORE, entity.toContentValues())
    }

    fun queryFromDatabase(query: String, args: Array<*>? = null) : Cursor {
        return database.query(query, args)
    }

    override fun apply(base: Statement?, description: Description?): Statement {
        return object : Statement() {
            override fun evaluate() {
                try {
                    base?.evaluate()
                } finally {
                    database.close()
                }
            }

        }
    }
}