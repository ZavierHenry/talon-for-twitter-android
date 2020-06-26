package com.klinker.android.twitter_l.mockentities

import android.content.ContentValues
import com.klinker.android.twitter_l.data.sq_lite.FollowersSQLiteHelper

data class MockTransferFollower(override val mockEntity: MockFollower) : MockTransferEntity<MockFollower> {
    private val follower = mockEntity.follower

    override fun copyId(id: Long): MockTransferEntity<MockFollower> {
        return this.copy(mockEntity = mockEntity.copy( follower = follower.copy(id = id)))
    }

    override fun toSQLiteContentValues(id: Long): ContentValues {
        val user = follower.user
        return ContentValues().apply {
            put(FollowersSQLiteHelper.COLUMN_ACCOUNT, follower.account)
            put(FollowersSQLiteHelper.COLUMN_ID, user.userId)
            put(FollowersSQLiteHelper.COLUMN_NAME, user.name)
            put(FollowersSQLiteHelper.COLUMN_SCREEN_NAME, user.screenName)
            put(FollowersSQLiteHelper.COLUMN_PRO_PIC, user.profilePic)
        }
    }

}