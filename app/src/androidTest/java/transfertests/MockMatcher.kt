package transfertests

import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher


internal abstract class MockMatcher<T: MockEntity<T>> protected constructor(protected var expected: T) : TypeSafeMatcher<T>() {

    override fun matchesSafely(item: T): Boolean {
       return expected.anyMismatches(item)
    }

    override fun describeMismatchSafely(item: T, mismatchDescription: Description) {

        val mismatches = expected.showMismatches(item)

        mismatchDescription.appendText("The following fields have a mismatch:\n")
        mismatches.forEach { (tag, values) ->
            mismatchDescription.appendText("$tag: Expected ")
                    .appendValue(values.first)
                    .appendText(", Actual ")
                    .appendValue(values.second)
                    .appendText("\n")
        }
    }

    override fun describeTo(description: Description) {
        description.appendText("All fields of both items are equal")
    }

}
