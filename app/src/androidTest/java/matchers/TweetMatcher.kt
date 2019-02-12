package matchers

import com.klinker.android.twitter_l.data.roomdb.entities.Tweet


class TweetMatcher private constructor(expected: Tweet) : EntityMatcher<Tweet>(expected) {

    override fun getMismatches(actual: Tweet): Map<String, Mismatch> {
        return mapOf(
                "id" to makeMismatch(expected.twitterId, actual.twitterId),
                "text" to makeMismatch(expected.text, actual.text),
                "time" to makeMismatch(expected.time, actual.time),
                "urls" to makeMismatch(expected.urls, actual.urls),
                "users" to makeMismatch(expected.users, actual.users),
                "pictureUrls" to makeMismatch(expected.pictureUrls, actual.pictureUrls),
                "retweeter" to makeMismatch(expected.retweeter, actual.retweeter),
                "gifUrl" to makeMismatch(expected.gifUrl, actual.gifUrl),
                "mediaLength" to makeMismatch(expected.mediaLength, actual.mediaLength),
                "is_conversation" to makeMismatch(expected.isConversation, actual.isConversation),
                "like_count" to makeMismatch(expected.likeCount, actual.likeCount),
                "retweet_count" to makeMismatch(expected.retweetCount, actual.retweetCount),
                "client_source" to makeMismatch(expected.clientSource, actual.clientSource),
                "hashtags" to makeMismatch(expected.hashtags, actual.hashtags)
        ).filterNot { (_, values) -> values.first == values.second }
    }

    companion object {
        @JvmStatic fun matchesTweet(expected: Tweet) : TweetMatcher {
            return TweetMatcher(expected)
        }
    }

}