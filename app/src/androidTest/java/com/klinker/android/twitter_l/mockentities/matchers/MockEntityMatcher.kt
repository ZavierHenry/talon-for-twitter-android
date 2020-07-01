package com.klinker.android.twitter_l.mockentities.matchers

import com.klinker.android.twitter_l.mockentities.MockEntity
import org.hamcrest.Description
import org.hamcrest.TypeSafeDiagnosingMatcher

class MockEntityMatcher<T>(private val expected: T) : TypeSafeDiagnosingMatcher<T>() where T : MockEntity {

    override fun matchesSafely(item: T, mismatchDescription: Description?): Boolean {
        val itemContentValues = item.toContentValues().apply {
            put("id", item.id)
        }

        val expectedContentValues = expected.toContentValues().apply {
            put("id", expected.id)
        }

        val difference = expectedContentValues.valueSet().subtract(itemContentValues.valueSet())

        for (field in difference) {
            val fieldKey = field.key
            val expectedField = field.value
            val actualField = itemContentValues.get(fieldKey)
            mismatchDescription?.appendText("Field: $fieldKey, Expected: $expectedField, Actual: ${actualField}\n")
        }

        return difference.isEmpty()

    }

    override fun describeTo(description: Description?) {
        description?.appendText("Both entities to have the value: ")
        description?.appendValue(expected)
    }

    companion object {
        fun<T> matchesMockEntity(expected: T) : MockEntityMatcher<T> where T : MockEntity {
            return MockEntityMatcher(expected)
        }
    }

}