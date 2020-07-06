package com.klinker.android.twitter_l.mockentities.transferentities

import android.content.ContentValues
import com.klinker.android.twitter_l.data.roomdb.User
import com.klinker.android.twitter_l.data.sq_lite.FavoriteUsersSQLiteHelper
import com.klinker.android.twitter_l.mockentities.MockFavoriteUser
import com.klinker.android.twitter_l.mockentities.MockUtilities


data class MockTransferFavoriteUser(override val mockEntity: MockFavoriteUser) : MockTransferEntity<MockFavoriteUser> {
    private val favoriteUser = mockEntity.favoriteUser

    constructor(account: Int, user: User = MockUtilities.makeMockUser(), id: Long = 0) : this(MockFavoriteUser(account, user, id))

    override fun copyId(id: Long): MockTransferEntity<MockFavoriteUser> {
        return this.copy(mockEntity = mockEntity.copy(favoriteUser = favoriteUser.copy(id = id)))
    }

    override fun toSQLiteContentValues(id: Long): ContentValues {
        val user = favoriteUser.user
        return ContentValues().apply {
            put(FavoriteUsersSQLiteHelper.COLUMN_ID, user.userId)
            put(FavoriteUsersSQLiteHelper.COLUMN_ACCOUNT, favoriteUser.account)
            put(FavoriteUsersSQLiteHelper.COLUMN_SCREEN_NAME, user.screenName)
            put(FavoriteUsersSQLiteHelper.COLUMN_NAME, user.name)
            put(FavoriteUsersSQLiteHelper.COLUMN_PRO_PIC, user.profilePic)
        }
    }

}