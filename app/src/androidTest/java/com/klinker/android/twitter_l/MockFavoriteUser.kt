package com.klinker.android.twitter_l

import android.content.ContentValues
import android.database.Cursor
import com.klinker.android.twitter_l.data.roomdb.FavoriteUser


data class MockFavoriteUser(val favoriteUser: FavoriteUser) : MockEntity {
    override fun toContentValues(): ContentValues {
        val userContentValues = MockUtilities.userToContentValues(favoriteUser.user)
        return ContentValues().apply {
            putAll(userContentValues)
            put("account", favoriteUser.account)
        }
    }

    companion object {
        fun toMockFavoriteUser(cursor: Cursor) : MockFavoriteUser {
            TODO("Not yet implemented")
        }
    }

}