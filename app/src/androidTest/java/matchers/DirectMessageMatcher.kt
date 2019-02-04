package matchers

import com.klinker.android.twitter_l.data.roomdb.pojos.DisplayDirectMessage

class DirectMessageMatcher


class DisplayDirectMessageMatcher private constructor(expected: DisplayDirectMessage) : EntityMatcher<DisplayDirectMessage>(expected) {
    override fun getMismatches(actual: DisplayDirectMessage): Map<String, Mismatch> {
        val senderMismatches = UserMatcher.matchesUser(expected.sender).getMismatches(actual.sender)
        val recipientMismatches = UserMatcher.matchesUser(expected.recipient).getMismatches(actual.recipient)

        return mapOf(
                "id" to makeMismatch(expected.directMessageId, actual.directMessageId),
                "message_id" to makeMismatch(expected.messageId, actual.messageId),
                "time" to makeMismatch(expected.time, actual.time),
                "text" to makeMismatch(expected.text, actual.text),
                "picture_urls" to makeMismatch(expected.pictureUrls, actual.pictureUrls),
                "urls" to makeMismatch(expected.urls, actual.urls),
                "gif_url" to makeMismatch(expected.gifUrl, actual.gifUrl),
                "media_length" to makeMismatch(expected.mediaLength, actual.mediaLength)
        ).filterNot { (_, values) -> values.first == values.second }.plus(senderMismatches).plus(recipientMismatches)
    }

    companion object {
        @JvmStatic fun matchesDisplayDirectMessage(expected: DisplayDirectMessage) : DisplayDirectMessageMatcher {
            return DisplayDirectMessageMatcher(expected)
        }
    }

}