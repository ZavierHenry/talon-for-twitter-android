package daotests


import android.content.Context

import com.klinker.android.twitter_l.data.roomdb.TalonDatabase

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry

abstract class DaoTest {
    companion object {
        var testDatabase: TalonDatabase? = null

        fun initTestDatabase() {
            val context = InstrumentationRegistry.getInstrumentation().targetContext
            testDatabase = Room.inMemoryDatabaseBuilder(context, TalonDatabase::class.java).build()
        }

        fun clearTestDatabase() {
            testDatabase?.clearAllTables()
        }


        fun closeTestDatabase() {
            testDatabase?.close()
            testDatabase = null
        }
    }

}
