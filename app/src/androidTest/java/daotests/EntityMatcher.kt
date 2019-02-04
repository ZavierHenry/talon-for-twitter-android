package daotests

import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher


typealias Mismatch = Pair<Any?, Any?>


internal abstract class EntityMatcher<T> protected constructor(protected var expected: T) : TypeSafeMatcher<T>() {

    override fun matchesSafely(item: T): Boolean {
        return anyMismatches(item)
    }

    override fun describeMismatchSafely(item: T, mismatchDescription: Description?) {

        val mismatches = getMismatches(item)

        mismatchDescription?.appendText("The following fields have a mismatch\n")
        mismatches.forEach { (tag, mismatch) ->
            mismatchDescription?.appendText("   $tag - Expected ")
                    ?.appendValue(mismatch.first)
                    ?.appendText(" but was actually ")
                    ?.appendValue(mismatch.second)
                    ?.appendText("\n")
        }
    }


    override fun describeTo(description: Description?) {
        description?.appendText("All fields of both items are equal")
    }

    abstract fun getMismatches(actual: T) : Map<String, Mismatch>
    private fun anyMismatches(actual: T) : Boolean {
        return getMismatches(actual).isEmpty()
    }
}
