package com.klinker.android.twitter_l.mockentities.transferentities

import android.content.ContentValues
import com.klinker.android.twitter_l.data.sq_lite.FavoriteUserNotificationSQLiteHelper
import com.klinker.android.twitter_l.mockentities.MockFavoriteUserNotification

data class MockTransferFavoriteUserNotification(override val mockEntity: MockFavoriteUserNotification) : MockTransferEntity<MockFavoriteUserNotification> {
    private val favoriteUserNotification = mockEntity.favoriteUserNotification

    constructor(tweetId: Long = 1L, id: Long = 0) : this(MockFavoriteUserNotification(tweetId, id))

    override fun copyId(id: Long): MockTransferEntity<MockFavoriteUserNotification> {
        return this.copy(mockEntity = mockEntity.copy(favoriteUserNotification = favoriteUserNotification.copy(id = id)))
    }

    override fun toSQLiteContentValues(id: Long): ContentValues {
        return ContentValues().apply {
            put(FavoriteUserNotificationSQLiteHelper.COLUMN_TWEET_ID, favoriteUserNotification.tweetId)

            if (id != -1L) {
                put(FavoriteUserNotificationSQLiteHelper.COLUMN_ID, id)
            }
        }
    }
}