package transfertests

import android.content.ContentValues
import android.database.Cursor

import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

internal class MockTweet(

): MockEntity<MockTweet>() {
    override fun showMismatches(other: MockTweet): List<FieldMismatch> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
