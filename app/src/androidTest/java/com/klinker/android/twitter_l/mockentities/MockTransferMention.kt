package com.klinker.android.twitter_l.mockentities

import android.content.ContentValues
import com.klinker.android.twitter_l.data.roomdb.ListStringConverter
import com.klinker.android.twitter_l.data.roomdb.Mention
import com.klinker.android.twitter_l.data.roomdb.Tweet
import com.klinker.android.twitter_l.data.sq_lite.MentionsSQLiteHelper

data class MockTransferMention(override val mockEntity: MockMention) : MockTransferEntity<MockMention> {

    private val mention = mockEntity.mention

    constructor(account: Int, isUnread: Boolean = true, tweet: Tweet = MockUtilities.makeMockTweet(), id: Long = -1) :
            this(MockMention(account, isUnread, tweet, id))

    override fun copyId(id: Long): MockTransferEntity<MockMention> {
        return this.copy(mockEntity = mockEntity.copy(mention = mention.copy(id = id)))
    }

    override fun toSQLiteContentValues(id: Long): ContentValues {
        val tweet = mention.tweet
        return ContentValues().apply {

            put(MentionsSQLiteHelper.COLUMN_ACCOUNT, mention.account)
            put(MentionsSQLiteHelper.COLUMN_TEXT, tweet.text)
            put(MentionsSQLiteHelper.COLUMN_TWEET_ID, tweet.tweetId)
            put(MentionsSQLiteHelper.COLUMN_NAME, tweet.author.name)
            put(MentionsSQLiteHelper.COLUMN_PRO_PIC, tweet.author.profilePic)
            put(MentionsSQLiteHelper.COLUMN_SCREEN_NAME, tweet.author.screenName)
            put(MentionsSQLiteHelper.COLUMN_TIME, tweet.time)
            put(MentionsSQLiteHelper.COLUMN_RETWEETER, "")
            put(MentionsSQLiteHelper.COLUMN_UNREAD, mention.isUnread)

            put(MentionsSQLiteHelper.COLUMN_PIC_URL, tweet.images.joinToString(" "))
            put(MentionsSQLiteHelper.COLUMN_URL, tweet.urls.joinToString("  "))
            put(MentionsSQLiteHelper.COLUMN_USERS, tweet.mentions.joinToString("  "))
            put(MentionsSQLiteHelper.COLUMN_HASHTAGS, tweet.hashtags.joinToString("  "))

            put(MentionsSQLiteHelper.COLUMN_CONVERSATION, tweet.isReply)
            put(MentionsSQLiteHelper.COLUMN_ANIMATED_GIF, tweet.gifUrl ?: "")
            put(MentionsSQLiteHelper.COLUMN_MEDIA_LENGTH, tweet.mediaLength ?: -1)

            if (id != -1L) {
                put(MentionsSQLiteHelper.COLUMN_ID, id)
            }
        }
    }
}