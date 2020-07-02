package com.klinker.android.twitter_l.mockentities.matchers

import com.klinker.android.twitter_l.mockentities.MockEntity
import org.hamcrest.Description
import org.hamcrest.TypeSafeDiagnosingMatcher
import org.hamcrest.TypeSafeMatcher

class MockEntityMatcher<T>(private val expected: T) : TypeSafeDiagnosingMatcher<T>() where T : MockEntity {


    override fun describeTo(description: Description?) {
        description?.appendText("Both entities to have the same values")
    }


    companion object {
        fun<T> matchesMockEntity(expected: T) : MockEntityMatcher<T> where T : MockEntity {
            return MockEntityMatcher(expected)
        }
    }

    override fun matchesSafely(item: T, mismatchDescription: Description?): Boolean {

        val itemContentValues = item.toContentValues().apply {
            put("id", item.id)
        }

        val expectedContentValues = expected.toContentValues().apply {
            put("id", expected.id)
        }

        val difference = expectedContentValues.valueSet().subtract(itemContentValues.valueSet())

        for ((index, field) in difference.withIndex()) {
            val fieldKey = field.key
            val expectedField = field.value
            val actualField = itemContentValues.get(fieldKey)
            mismatchDescription
                    ?.appendText(if (index == 0) "Property " else "property ")?.appendValue(fieldKey)
                    ?.appendText(" expected the value ")?.appendValue(expectedField)
                    ?.appendText(" but was actually ")?.appendValue(actualField)

            if (index < difference.size - 1) {
                mismatchDescription?.appendText(" and\n")
            }

        }

        return difference.isEmpty()
    }

}