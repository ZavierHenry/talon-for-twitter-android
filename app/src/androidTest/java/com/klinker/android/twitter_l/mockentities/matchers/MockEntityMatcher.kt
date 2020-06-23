package com.klinker.android.twitter_l.mockentities.matchers

import com.klinker.android.twitter_l.mockentities.MockEntity
import org.hamcrest.Description
import org.hamcrest.TypeSafeDiagnosingMatcher
import org.hamcrest.TypeSafeMatcher

class MockEntityMatcher<T>(private val expected: T) : TypeSafeDiagnosingMatcher<T>() where T : MockEntity {

    override fun matchesSafely(item: T, mismatchDescription: Description?): Boolean {
        val itemContentValues = item.toContentValues().apply {
            put("id", item.id)
        }

        val expectedContentValues = expected.toContentValues().apply {
            put("id", expected.id)
        }

        val difference = expectedContentValues.valueSet().subtract(itemContentValues.valueSet())

        for (property in difference) {
            val key = property.key
            val expected = property.value
            val actual = itemContentValues.get(key)
            mismatchDescription?.appendText("Field: $key, Expected: $expected, Actual: $actual\n")
        }

        return difference.isEmpty()

    }

    override fun describeTo(description: Description?) {
        description?.appendText("Both entities to have the same values")
    }

    companion object {
        fun<T> matchesMockEntity(expected: T) : MockEntityMatcher<T> where T : MockEntity {
            return MockEntityMatcher(expected)
        }
    }

}