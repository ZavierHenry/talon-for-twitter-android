package transfertests


import android.content.Context
import android.database.sqlite.SQLiteDatabase

import com.klinker.android.twitter_l.data.roomdb.TalonDatabase


import java.io.File

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.test.platform.app.InstrumentationRegistry

abstract class TransferTest {

    internal fun applyCallback(callback: RoomDatabase.Callback) {
        callback.onCreate(testDatabase!!.openHelper.writableDatabase)
    }

    companion object {

        private const val relativeDbPath = "test_database.db"
        internal var testDatabase: TalonDatabase? = null
        internal var sourceDatabase: SQLiteDatabase? = null
        internal const val badDatabaseLocation = "no_database_here.db"

        fun initTestDatabase() {
            val context = InstrumentationRegistry.getInstrumentation().targetContext
            testDatabase = Room.inMemoryDatabaseBuilder(context, TalonDatabase::class.java).build()
        }

        fun initSourceDatabase(vararg tableCreationStatements: String) {

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


        fun clearTestDatabase() {
            testDatabase?.clearAllTables()
        }

        fun clearSourceDatabase(tableName: String) {
            sourceDatabase?.delete(tableName, null, null)
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
