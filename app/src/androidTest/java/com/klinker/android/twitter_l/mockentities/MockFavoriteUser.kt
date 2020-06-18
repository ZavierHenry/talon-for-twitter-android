package com.klinker.android.twitter_l.mockentities

import android.content.ContentValues
import android.database.Cursor
import com.klinker.android.twitter_l.data.roomdb.FavoriteUser
import com.klinker.android.twitter_l.data.roomdb.User

data class MockFavoriteUser(val favoriteUser: FavoriteUser) : MockEntity {

    constructor(account: Int, user: User = MockUtilities.makeMockUser(), id: Long? = null) : this(FavoriteUser(user, account, id))

    constructor(cursor: Cursor) : this(
            cursor.getInt(cursor.getColumnIndex("account")),
            MockUtilities.cursorToUser(cursor),
            cursor.getLong(cursor.getColumnIndex("id"))
    )

    override fun toContentValues(): ContentValues {
        val userContentValues = MockUtilities.userToContentValues(favoriteUser.user)
        return ContentValues().apply {
            putAll(userContentValues)
            put("account", favoriteUser.account)
        }
    }

}