package com.klinker.android.twitter_l.transfertests

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.test.core.app.ApplicationProvider
import com.klinker.android.twitter_l.data.roomdb.TalonDatabase
import com.klinker.android.twitter_l.data.roomdb.transfers.TalonDatabaseCallback
import com.klinker.android.twitter_l.mockentities.MockEntity
import com.klinker.android.twitter_l.mockentities.MockTransferEntity
import org.junit.rules.TemporaryFolder
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class TestTransferDatabase(private val sourceTableName: String, private val destTableName: String, callback: TalonDatabaseCallback, columns: List<String>, private val primaryKey: String? = null) : TestRule {

    private var sourceDatabase: SQLiteDatabase? = null
    private var destDatabase: TalonDatabase? = null
    private val createTableSql = columns.joinToString(
            prefix = "create table $sourceTableName (",
            separator = ", ",
            postfix = ");"
    )

    private val testCallback = TestCallback(callback)

    val sourceSize: Int
        get() = sourceDatabase?.query(sourceTableName, arrayOf("COUNT(1)"), null, null, null, null, null)?.use { cursor ->
            cursor.moveToFirst()
            cursor.getInt(0)
        } ?: 0

    val destSize: Int
        get() = destDatabase?.query("SELECT COUNT(1) FROM $destTableName", emptyArray())?.use { cursor ->
            cursor.moveToFirst()
            cursor.getInt(0)
        } ?: 0


    fun insertIntoSQLiteDatabase(mockTransferEntity: MockTransferEntity<MockEntity>) : Long? {
        val contentValues = mockTransferEntity.toSQLiteContentValues()
        val id = sourceDatabase?.insert(sourceTableName, null, contentValues)
        return if (id == null || id == -1L) null else id
    }

    fun buildDestinationDatabase() {
        destDatabase = with(ApplicationProvider.getApplicationContext<Context>()) {
            Room.inMemoryDatabaseBuilder(this, TalonDatabase::class.java)
                    .addCallback(testCallback)
                    .build()
        }
    }

    fun queryFromTalonDatabase(query: String, args: Array<*>? = null) : Cursor? {
        return destDatabase?.query(query, args)
    }

    override fun apply(base: Statement?, description: Description?): Statement {
        val temporaryFolder = TemporaryFolder()

        val baseStatement = object : Statement() {
            override fun evaluate() {
                try {
                    val tempFile = temporaryFolder.newFile("test_database.db")
                    sourceDatabase = SQLiteDatabase.openOrCreateDatabase(tempFile, null).apply {
                        execSQL(createTableSql)
                    }
                    base?.evaluate()
                } finally {
                    destDatabase?.close()
                    destDatabase = null
                    sourceDatabase?.close()
                    sourceDatabase = null
                }
            }
        }

        return temporaryFolder.apply(baseStatement, description)

    }

    private inner class TestCallback(private val callback: TalonDatabaseCallback) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            sourceDatabase?.query(sourceTableName, null, null, null, null, null, primaryKey?.let { "$it ASC" })?.use { cursor ->
                cursor.moveToFirst()
                while (!cursor.isAfterLast) {
                    callback.onEachTableRow(cursor, db)
                    cursor.moveToNext()
                }
            }
        }
    }



}