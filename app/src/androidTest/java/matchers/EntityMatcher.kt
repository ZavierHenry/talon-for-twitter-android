package matchers

import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher


typealias Mismatch = Pair<Any?, Any?>


abstract class EntityMatcher<T> protected constructor(protected val expected: T) : TypeSafeMatcher<T>() {


    override fun describeTo(description: Description?) {
        description?.appendText("All fields of both items are equal")
    }

    override fun matchesSafely(actual: T): Boolean {
        return getMismatches(actual).isEmpty()
    }

    override fun describeMismatchSafely(item: T, mismatchDescription: Description?) {

        val mismatches = getMismatches(item)

        mismatches.forEach { tag, (expected, actual) ->
            mismatchDescription?.appendText("$tag: Expected ")
                    ?.appendValue(expected)
                    ?.appendText(", Actual ")
                    ?.appendValue(actual)
                    ?.appendText("\n")
        }

    }

    abstract fun getMismatches(actual: T) : Map<String, Mismatch>

    protected fun makeMismatch(expected: Any?, actual: Any?) : Mismatch {
        return expected to actual
    }

}