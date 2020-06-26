package com.klinker.android.twitter_l.mockentities

import android.content.ContentValues
import com.klinker.android.twitter_l.data.sq_lite.QueuedSQLiteHelper

data class MockTransferDraft(override val mockEntity: MockDraft) : MockTransferEntity<MockDraft> {

    private val draft = mockEntity.draft

    constructor(account: Int, text: String = "", id: Long = 0) : this(MockDraft(account, text, id))

    override fun copyId(id: Long) : MockTransferDraft {
        return this.copy(mockEntity = mockEntity.copy(draft = draft.copy(id = id)))
    }

    override fun toSQLiteContentValues(id: Long): ContentValues {
        return ContentValues().apply {
            put(QueuedSQLiteHelper.COLUMN_ACCOUNT, draft.account)
            put(QueuedSQLiteHelper.COLUMN_TEXT, draft.text)
            put(QueuedSQLiteHelper.COLUMN_TIME, 0L)
            put(QueuedSQLiteHelper.COLUMN_ALARM_ID, 0L)
            put(QueuedSQLiteHelper.COLUMN_TYPE, QueuedSQLiteHelper.TYPE_DRAFT)

            if (id != -1L) {
                put(QueuedSQLiteHelper.COLUMN_ID, id)
            }
        }
    }

}