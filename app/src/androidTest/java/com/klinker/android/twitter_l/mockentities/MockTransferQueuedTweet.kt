package com.klinker.android.twitter_l.mockentities

import android.content.ContentValues
import com.klinker.android.twitter_l.data.sq_lite.QueuedSQLiteHelper

data class MockTransferQueuedTweet(override val mockEntity: MockQueuedTweet) : MockTransferEntity<MockQueuedTweet> {
    private val queuedTweet = mockEntity.queuedTweet

    override fun copyId(id: Long): MockTransferEntity<MockQueuedTweet> {
        return this.copy(mockEntity = mockEntity.copy(queuedTweet = queuedTweet.copy(id = id)))
    }

    override fun toSQLiteContentValues(id: Long): ContentValues {
        return ContentValues().apply {
            put(QueuedSQLiteHelper.COLUMN_ACCOUNT, queuedTweet.account)
            put(QueuedSQLiteHelper.COLUMN_TEXT, queuedTweet.text)
            put(QueuedSQLiteHelper.COLUMN_TIME, 0L)
            put(QueuedSQLiteHelper.COLUMN_ALARM_ID, 0L)
            put(QueuedSQLiteHelper.COLUMN_TYPE, QueuedSQLiteHelper.TYPE_QUEUED_TWEET)
        }
    }
}