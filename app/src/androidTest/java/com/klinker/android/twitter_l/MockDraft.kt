package com.klinker.android.twitter_l

import android.database.Cursor
import com.klinker.android.twitter_l.data.roomdb.Draft


class MockDraft(account: Int, text: String = "", id: Long? = null) : MockEntity {
    val draft: Draft = Draft(text, account, id)
    override fun toArgs(): Array<Any> {
        TODO("Not yet implemented")
    }

    companion object {
        const val tableName = "drafts"
        fun toMockDraft(cursor: Cursor) : MockDraft {
            return MockDraft(1)
        }
    }

}