package transfertests


import android.database.Cursor

import org.hamcrest.CustomTypeSafeMatcher
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

internal class MockUser {

    var id: Long = 0
    var name: String = ""
    var screenName: String = ""
    var profilePic: String = ""
    var isVerified: Boolean = false


    constructor(id: Long, name: String, screenName: String, profilePic: String, isVerified: Boolean) {
        this.id = id
        this.name = name
        this.screenName = screenName
        this.profilePic = profilePic
        this.isVerified = isVerified
    }

    constructor(cursor: Cursor) {
        this.id = cursor.getLong(cursor.getColumnIndex("users.id"))
        this.name = cursor.getString(cursor.getColumnIndex("users.name"))
        this.screenName = cursor.getString(cursor.getColumnIndex("users.screen_name"))
        this.profilePic = cursor.getString(cursor.getColumnIndex("users.profile_pic"))
        this.isVerified = cursor.getInt(cursor.getColumnIndex("users.is_verified")) == 1
    }


}


internal class MockUserMatcher private constructor(private val expected: MockUser) : TypeSafeMatcher<MockUser>() {

    public override fun matchesSafely(item: MockUser): Boolean {
        return false
    }

    override fun describeTo(description: Description) {
        description.appendText("All user fields should match")
    }

    override fun describeMismatchSafely(item: MockUser, description: Description) {
        description.appendText("The following field have a mismatch:\n")


        if (expected.isVerified != item.isVerified) {

        }

        if (expected.id != item.id) {

        }

        if (expected.name != item.name) {

        }

        if (expected.screenName != item.screenName) {

        }

        if (expected.profilePic != item.profilePic) {

        }

    }

    companion object {

        fun matchesUser(expected: MockUser): MockUserMatcher {
            return MockUserMatcher(expected)
        }
    }

}
