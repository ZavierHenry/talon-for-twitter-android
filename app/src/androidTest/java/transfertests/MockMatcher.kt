package transfertests

import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

internal abstract class MockMatcher<T> protected constructor(protected var expected: T) : TypeSafeMatcher<T>() {

    abstract override fun matchesSafely(item: T): Boolean

    abstract override fun describeMismatchSafely(item: T, mismatchDescription: Description)

    override fun describeTo(description: Description) {
        description.appendText("All fields of both items are equal")
    }

}
