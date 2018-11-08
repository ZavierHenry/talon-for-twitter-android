package daotests


import android.content.ContentValues
import android.database.Cursor

import com.klinker.android.twitter_l.data.roomdb.TalonDatabase

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry

abstract class DaoTest {

    fun queryDatabase(query: String, args: Array<Any>?) : Cursor {
        return testDatabase.query(query, args)
    }

    fun insertIntoDatabase(tableName: String, conflictAlgorithm: Int, values: ContentValues): Long {
        return testDatabase.openHelper.writableDatabase.insert(tableName, conflictAlgorithm, values)
    }

    fun beginTransaction() {
        testDatabase.openHelper.writableDatabase.beginTransaction()
    }

    fun endSuccessfulTransaction() {
        testDatabase.openHelper.writableDatabase.setTransactionSuccessful()
        testDatabase.openHelper.writableDatabase.endTransaction()
    }


    fun clearTestDatabase() {
        testDatabase.clearAllTables()
    }


    companion object {
        lateinit var testDatabase: TalonDatabase

        fun initTestDatabase() {
            val context = InstrumentationRegistry.getInstrumentation().targetContext
            testDatabase = Room.inMemoryDatabaseBuilder(context, TalonDatabase::class.java).build()
        }

        fun closeTestDatabase() {
            testDatabase.close()
        }
    }

}
