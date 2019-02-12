package daotests


import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

import com.klinker.android.twitter_l.data.roomdb.TalonDatabase

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.klinker.android.twitter_l.data.roomdb.entities.Tweet
import com.klinker.android.twitter_l.data.roomdb.entities.User
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import java.lang.Exception


abstract class DaoTest {

    fun queryDatabase(query: String, args: Array<Any>?) : Cursor {
        return testDatabase.query(query, args)
    }

    fun deleteFromDatabase(table: String, where: String, args: Array<Any>?) : Int {
        return try {
            testDatabase.openHelper.writableDatabase.delete(table, where, args)
        } catch (_: Exception) {
            0
        }
    }

    fun insertIntoDatabase(tableName: String, conflictAlgorithm: Int, values: ContentValues): Long {
        return testDatabase.openHelper.writableDatabase.insert(tableName, conflictAlgorithm, values)
    }


    inline fun <R> asTransaction(block: () -> R) : R {
        testDatabase.openHelper.writableDatabase.beginTransaction()

        val result = block()

        testDatabase.openHelper.writableDatabase.setTransactionSuccessful()
        testDatabase.openHelper.writableDatabase.endTransaction()

        return result

    }

    fun clearTestDatabase() {
        testDatabase.clearAllTables()
    }


    companion object {
        lateinit var testDatabase: TalonDatabase

        @JvmStatic internal fun initTestDatabase() {
            val context = InstrumentationRegistry.getInstrumentation().targetContext
            testDatabase = Room.inMemoryDatabaseBuilder(context, TalonDatabase::class.java).build()
        }

        @JvmStatic internal fun closeTestDatabase() {
            testDatabase.close()
        }
    }

}
