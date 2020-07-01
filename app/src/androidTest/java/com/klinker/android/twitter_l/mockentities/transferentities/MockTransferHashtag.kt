package com.klinker.android.twitter_l.mockentities.transferentities

import android.content.ContentValues
import com.klinker.android.twitter_l.data.sq_lite.HashtagSQLiteHelper
import com.klinker.android.twitter_l.mockentities.MockHashtag


data class MockTransferHashtag(override val mockEntity: MockHashtag) : MockTransferEntity<MockHashtag> {
    private val hashtag = mockEntity.hashtag

    constructor(tag: String, id: Long = 0) : this(MockHashtag(tag, id))

    override fun copyId(id: Long): MockTransferEntity<MockHashtag> {
        return this.copy(mockEntity = mockEntity.copy(hashtag = hashtag.copy(id = id)))
    }

    override fun toSQLiteContentValues(id: Long): ContentValues {
        return ContentValues().apply {
            put(HashtagSQLiteHelper.COLUMN_TAG, hashtag.tag)
        }
    }

}