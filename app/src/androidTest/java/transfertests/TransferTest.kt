package transfertests


import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.room.OnConflictStrategy

import com.klinker.android.twitter_l.data.roomdb.TalonDatabase


import java.io.File

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.test.platform.app.InstrumentationRegistry

abstract class TransferTest {

    val sourceDatabasePath : String
        get() = sourceDatabase!!.path

    internal fun applyCallback(callback: RoomDatabase.Callback) {
        callback.onCreate(testDatabase!!.openHelper.writableDatabase)
    }

    internal fun queryTestDatabase(query : String, args : Array<Any>?) : Cursor {
        return testDatabase!!.query(query, args)
    }

    internal fun beginSourceDatabaseTransaction() {
        sourceDatabase!!.beginTransaction()
    }

    internal fun endSuccessfulSourceDatabaseTransaction() {
        sourceDatabase!!.setTransactionSuccessful()
        sourceDatabase!!.endTransaction()
    }

    internal fun insertIntoSourceDatabase(tableName : String, values : ContentValues) : Long {
        return sourceDatabase!!.insert(tableName, null, values)
    }


    internal fun clearTestDatabase() {
        testDatabase?.clearAllTables()
    }

    internal fun clearSourceDatabase(tableName: String) {
        sourceDatabase?.delete(tableName, null, null)
    }


    companion object {

        private const val relativeDbPath = "test_database.db"
        internal var testDatabase: TalonDatabase? = null
        internal var sourceDatabase: SQLiteDatabase? = null
        internal const val badDatabaseLocation = "no_database_here.db"

        internal fun initTestDatabase() {
            val context = InstrumentationRegistry.getInstrumentation().targetContext
            testDatabase = Room.inMemoryDatabaseBuilder(context, TalonDatabase::class.java).build()
        }

        internal fun initSourceDatabase(vararg tableCreationStatements: String) {

            val fullDbPath = InstrumentationRegistry
                    .getInstrumentation()
                    .targetContext
                    .getDatabasePath(relativeDbPath)

            if (fullDbPath.exists()) {
                SQLiteDatabase.deleteDatabase(fullDbPath)
            }

            sourceDatabase = SQLiteDatabase.openOrCreateDatabase(fullDbPath, null)
            for (statement in tableCreationStatements) {
                sourceDatabase!!.execSQL(statement)
            }

        }

        internal fun closeTestDatabase() {
            testDatabase?.close()
            testDatabase = null
        }

        internal fun closeSourceDatabase() {
            sourceDatabase?.close()
            val path = sourceDatabase?.path
            path ?: SQLiteDatabase.deleteDatabase(File(path))
            sourceDatabase = null
        }
    }

}
