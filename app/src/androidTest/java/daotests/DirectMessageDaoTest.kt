package daotests

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.room.Dao
import com.klinker.android.twitter_l.data.roomdb.daos.DirectMessageDao
import com.klinker.android.twitter_l.data.roomdb.entities.DirectMessage
import com.klinker.android.twitter_l.data.roomdb.entities.User
import com.klinker.android.twitter_l.data.roomdb.pojos.DisplayDirectMessage
import matchers.DisplayDirectMessageMatcher
import matchers.DisplayDirectMessageMatcher.Companion.matchesDisplayDirectMessage
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.*

class DirectMessageDaoTest : DaoTest()