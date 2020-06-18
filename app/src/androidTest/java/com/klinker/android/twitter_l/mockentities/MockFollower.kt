package com.klinker.android.twitter_l.mockentities

import android.content.ContentValues
import android.database.Cursor
import com.klinker.android.twitter_l.data.roomdb.Follower
import com.klinker.android.twitter_l.data.roomdb.User

data class MockFollower(val follower: Follower) : MockEntity {

   constructor(account: Int, user: User = MockUtilities.makeMockUser(), id: Long? = null) :
           this(Follower(user, account, id))

    constructor(cursor: Cursor) : this(
            cursor.getInt(cursor.getColumnIndex("account")),
            MockUtilities.cursorToUser(cursor),
            cursor.getLong(cursor.getColumnIndex("id"))
    )

    override fun toContentValues(): ContentValues {
        val user = MockUtilities.userToContentValues(follower.user)
        return ContentValues().apply {
            putAll(user)
            put("account", follower.account)
        }
    }
}