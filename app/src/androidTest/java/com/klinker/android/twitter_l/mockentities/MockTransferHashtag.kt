package com.klinker.android.twitter_l.mockentities

import android.content.ContentValues
import com.klinker.android.twitter_l.data.sq_lite.HashtagSQLiteHelper


data class MockTransferHashtag(override val mockEntity: MockHashtag) : MockTransferEntity<MockHashtag> {
    private val hashtag = mockEntity.hashtag

    override fun copyId(id: Long): MockTransferEntity<MockHashtag> {
        return this.copy(mockEntity = mockEntity.copy(hashtag = hashtag.copy(id = id)))
    }

    override fun toSQLiteContentValues(id: Long): ContentValues {
        return ContentValues().apply {
            put(HashtagSQLiteHelper.COLUMN_TAG, hashtag.tag)
        }
    }

}