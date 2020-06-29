package com.klinker.android.twitter_l.mockentities.transferentities

import android.content.ContentValues
import com.klinker.android.twitter_l.data.sq_lite.QueuedSQLiteHelper
import com.klinker.android.twitter_l.mockentities.MockScheduledTweet

data class MockTransferScheduledTweet(override val mockEntity: MockScheduledTweet) : MockTransferEntity<MockScheduledTweet> {
    private val scheduledTweet = mockEntity.scheduledTweet

    constructor(account: Int, text: String = "", time: Long = 0L, alarmId: Long = 1L, id: Long = 0) :
            this(MockScheduledTweet(account, text, time, alarmId, id))

    override fun copyId(id: Long): MockTransferEntity<MockScheduledTweet> {
        return this.copy(mockEntity = mockEntity.copy(scheduledTweet = scheduledTweet.copy(id = id)))
    }

    override fun toSQLiteContentValues(id: Long): ContentValues {
        return ContentValues().apply {
            put(QueuedSQLiteHelper.COLUMN_ACCOUNT, scheduledTweet.account)
            put(QueuedSQLiteHelper.COLUMN_TEXT, scheduledTweet.text)
            put(QueuedSQLiteHelper.COLUMN_TIME, scheduledTweet.time)
            put(QueuedSQLiteHelper.COLUMN_ALARM_ID, scheduledTweet.alarmId)
        }
    }
}