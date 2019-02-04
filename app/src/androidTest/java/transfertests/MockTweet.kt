package transfertests

import android.content.ContentValues
import android.database.Cursor

import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

internal class MockTweet(
        var id : Long?,
        var name: String?,
        var screenName: String?,
        var date: Long?,
        var userId: Long?,
        var clientSource: String?,
        var retweeter: String?,
        var hashtags: String?,
        var users: String?,
        var urls: String?,
        var picUrls: String?,
        var gifUrl: String?,
        var mediaLength: Int?,
        var isInConversation: Boolean?

): MockEntity<MockTweet>() {
    override fun showMismatches(other: MockTweet): Collection<FieldMismatch> {

        val mismatches = ArrayList<FieldMismatch>()

        if (id != other.id) {
            mismatches.add(makeMismatch("id", id, other.id))
        }

        if (name != other.name) {
            mismatches.add(makeMismatch("name", name, other.name))
        }

        if (screenName != other.screenName) {
            mismatches.add(makeMismatch("screenName", screenName, other.screenName))
        }

        if (date != other.date) {
            mismatches.add(makeMismatch("date", date, other.date))
        }

        if (userId != other.userId) {
            mismatches.add(makeMismatch("userId", userId, other.userId))
        }

        if (clientSource != other.clientSource) {
            mismatches.add(makeMismatch("clientSource", clientSource, other.clientSource))
        }

        if (retweeter != other.retweeter) {
            mismatches.add(makeMismatch("retweeter", retweeter, other.retweeter))
        }

        if (hashtags != other.hashtags) {
            mismatches.add(makeMismatch("hashtags", hashtags, other.hashtags))
        }

        if (users != other.users) {
            mismatches.add(makeMismatch("users", users, other.users))
        }

        if (urls != other.urls) {
            mismatches.add(makeMismatch("urls", urls, other.urls))
        }

        if (picUrls != other.picUrls) {
            mismatches.add(makeMismatch("picUrls", picUrls, other.picUrls))
        }

        if (gifUrl != other.gifUrl) {
            mismatches.add(makeMismatch("gifUrl", gifUrl, other.gifUrl))
        }

        if (mediaLength != other.mediaLength) {
            mismatches.add(makeMismatch("mediaLength", mediaLength, other.mediaLength))
        }

        if (isInConversation != other.isInConversation) {
            mismatches.add(makeMismatch("isInConverastion", isInConversation, other.isInConversation))
        }

        return mismatches
    }

    override fun setContentValues(contentValues: ContentValues) {}

}

internal class MockTweetMatcher private constructor(expected: MockTweet) : MockMatcher<MockTweet>(expected) {

    companion object {

        fun matchesTweet(expected: MockTweet): MockTweetMatcher {
            return MockTweetMatcher(expected)
        }
    }
}
