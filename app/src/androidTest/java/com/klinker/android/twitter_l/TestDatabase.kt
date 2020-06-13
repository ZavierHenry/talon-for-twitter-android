package com.klinker.android.twitter_l

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.klinker.android.twitter_l.data.roomdb.TalonDatabase
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class TestDatabase : TestRule {

    val database = with(ApplicationProvider.getApplicationContext<Context>()) {
        Room.inMemoryDatabaseBuilder(this, TalonDatabase::class.java).build()
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