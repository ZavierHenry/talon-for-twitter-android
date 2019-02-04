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

    val context : Context get() = InstrumentationRegistry.getInstrumentation().context

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


    internal inline fun <R> asTransaction(block: () -> R) : R {

        return with(sourceDatabase!!) {
            beginTransaction()

            val result = block()

            setTransactionSuccessful()
            endTransaction()

            result
        }

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

        @JvmStatic internal fun initTestDatabase() {
            val context = InstrumentationRegistry.getInstrumentation().targetContext
            testDatabase = Room.inMemoryDatabaseBuilder(context, TalonDatabase::class.java).build()
        }

        @JvmStatic internal fun initSourceDatabase(vararg tableCreationStatements: String) {

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

        @JvmStatic internal fun closeTestDatabase() {
            testDatabase?.close()
            testDatabase = null
        }

        @JvmStatic internal fun closeSourceDatabase() {
            sourceDatabase?.close()
            sourceDatabase?.path?.also { SQLiteDatabase.deleteDatabase(File(it))}
            sourceDatabase = null
        }
    }

}
