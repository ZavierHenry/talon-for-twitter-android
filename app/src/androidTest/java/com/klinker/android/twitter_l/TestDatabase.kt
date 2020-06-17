package com.klinker.android.twitter_l

import android.content.Context
import android.database.Cursor
import androidx.room.OnConflictStrategy
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.klinker.android.twitter_l.data.roomdb.TalonDatabase
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class TestDatabase(val tableName: String) : TestRule {

    val database = with(ApplicationProvider.getApplicationContext<Context>()) {
        Room.inMemoryDatabaseBuilder(this, TalonDatabase::class.java).build()
    }

    val size: Int
        get() = database.query("SELECT COUNT(*) FROM $tableName", emptyArray()).use { cursor ->
                    cursor.moveToFirst()
                    cursor.getInt(0)
                }

    fun insertIntoDatabase(entity: MockEntity) : Long? {
        val id = database.openHelper.writableDatabase.insert(tableName, OnConflictStrategy.IGNORE, entity.toContentValues())
        return if (id == -1L) null else id
    }

    fun queryFromDatabase(query: String, args: Array<Any>? = null) : Cursor {
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