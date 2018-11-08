package transfertests

import android.database.Cursor

import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

internal class MockTweet {

    var date: Long = 0

    var text: String = ""

    var retweeter: String? = null
    var hashtags: String? = null
    var clientSource: String? = null

    var gifUrl: String? = null
    var mediaLength: Int = 0


    constructor(cursor: Cursor) {
        this.text = cursor.getString(cursor.getColumnIndex("tweet.text"))

    }


}

internal class MockTweetMatcher private constructor(private val expected: MockTweet) : TypeSafeMatcher<MockTweet>() {

    public override fun matchesSafely(item: MockTweet): Boolean {
        return false
    }

    override fun describeTo(description: Description) {

    }

    override fun describeMismatchSafely(item: MockTweet, description: Description) {

    }

    companion object {

        fun matchesTweet(expected: MockTweet): MockTweetMatcher {
            return MockTweetMatcher(expected)
        }
    }

}
