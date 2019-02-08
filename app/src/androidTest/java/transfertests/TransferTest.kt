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
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.anyOf
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.hamcrest.Matchers.greaterThan

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


    /*
        Common tests
     */

    protected fun testTransferIfEmptyTable(callback: RoomDatabase.Callback, tableName: String) {
        applyCallback(callback)
        queryTestDatabase("SELECT 1 FROM $tableName", null).use { cursor ->
            assertThat("Something went wrong in the test transfer", cursor.count, `is`(0))
        }
    }

    protected fun testTransferIfEmptySource(callbackGenerator: (Context, String) -> RoomDatabase.Callback) {
        applyCallback(callbackGenerator(context, badDatabaseLocation))
    }

    //testTransferIfEmptySource


    internal inline fun <T: MockEntity<T>> testBasicTransfer(callback: RoomDatabase.Callback, sourceTableName: String, destTableName: String, mockGenerator: (Int) -> T, matcherGenerator: (T) -> MockMatcher<T>, dbmockGenerator: (Cursor) -> T, size: Int = 3) {
        val entities = List(size, mockGenerator)

        val idCount = asTransaction {
            entities.count { entity ->
                insertIntoSourceDatabase(sourceTableName, ContentValues().apply { entity.setContentValues(this) }) != -1L
            }
        }

        assertThat("There must be at least one entry in table $sourceTableName to properly run this test", idCount, greaterThan(0))
        applyCallback(callback)

        queryTestDatabase("SELECT * FROM $destTableName", null).use { cursor ->
            assertThat("Incorrect number of entities transferred to table $destTableName", cursor.count, `is`(idCount))
            assertThat("Error getting the first value of the query", cursor.moveToFirst())
            val matchers = entities.map(matcherGenerator)

            do {

                val mockDbEntity = dbmockGenerator(cursor)
                assertThat("Source hashtag did not transfer to the test database", mockDbEntity, anyOf(matchers))

            } while (cursor.moveToNext())


        }

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
