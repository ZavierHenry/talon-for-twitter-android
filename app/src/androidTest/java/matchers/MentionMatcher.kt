package matchers

import com.klinker.android.twitter_l.data.roomdb.entities.Mention


class MentionMatcher private constructor(expected: Mention) : EntityMatcher<Mention>(expected) {
    override fun getMismatches(actual: Mention): Map<String, Mismatch> {
        return mapOf(
                "id" to makeMismatch(expected.id, actual.id),
                "account" to makeMismatch(expected.account, actual.account),
                "isUnread" to makeMismatch(expected.isUnread, actual.isUnread),
                "tweetId" to makeMismatch(expected.tweetId, actual.tweetId)
        ).filterNot { (_, match) -> match.first == match.second }

    }

    companion object {

        @JvmStatic fun matchesMention(expected: Mention) : MentionMatcher {
            return MentionMatcher(expected)
        }
    }

}