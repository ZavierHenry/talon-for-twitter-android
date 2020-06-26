package com.klinker.android.twitter_l.mockentities.matchers

import com.klinker.android.twitter_l.mockentities.MockEntity
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeDiagnosingMatcher


class EntityValidIdMatcher<T>() : TypeSafeDiagnosingMatcher<T>() where T : MockEntity {

    override fun describeTo(description: Description?) {
        description?.appendText("Entity id to be valid")
    }

    override fun matchesSafely(item: T, mismatchDescription: Description?): Boolean {
        return item.id != 0L && item.id != -1L
    }

    companion object {
        fun<T> hasValidId() : EntityValidIdMatcher<T> where T : MockEntity {
            return EntityValidIdMatcher()
        }

        fun<T> hasInvalidId() : Matcher<T> where T : MockEntity {
            return not(EntityValidIdMatcher<T>())
        }
    }

}

