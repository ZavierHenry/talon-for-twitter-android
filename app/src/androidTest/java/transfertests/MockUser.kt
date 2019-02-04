package transfertests


import android.content.ContentValues
import android.database.Cursor


internal class MockUser(var twitterId: Long?,
                        var name: String?,
                        var screenName: String?,
                        var profilePic: String?,
                        var isVerified: Boolean = false) : MockEntity<MockUser>() {

    override fun showMismatches(other: MockUser): Collection<FieldMismatch> {

        val mismatches = ArrayList<FieldMismatch>()

        if (twitterId != other.twitterId) {
            mismatches.add(makeMismatch("twitterId", twitterId, other.twitterId))
        }

        if (name != other.name) {
            mismatches.add(makeMismatch("name", name, other.name))
        }

        if (screenName != other.screenName) {
            mismatches.add(makeMismatch("screenName", screenName, other.screenName))
        }

        if (profilePic != other.profilePic) {
            mismatches.add(makeMismatch("profilePic", profilePic, other.profilePic))
        }

        if (isVerified != other.isVerified) {
            mismatches.add(makeMismatch("isVerified", isVerified, other.isVerified))
        }

        return mismatches.toList()

    }

    override fun setContentValues(contentValues: ContentValues) {}


    companion object {

        @JvmStatic fun create(cursor: Cursor) : MockUser {
            val twitterId = cursor.getLong(cursor.getColumnIndex("twitter_id"))
            val name = cursor.getString(cursor.getColumnIndex("name"))
            val screenName = cursor.getString(cursor.getColumnIndex("screen_name"))
            val profilePic = cursor.getString(cursor.getColumnIndex("profile_pic"))
            val isVerified = cursor.getInt(cursor.getColumnIndex("is_verified")) == 1

            return MockUser(twitterId, name, screenName, profilePic, isVerified)
        }

    }

}


internal class MockUserMatcher private constructor(expected: MockUser) : MockMatcher<MockUser>(expected) {

    companion object {

        fun matchesUser(expected: MockUser): MockUserMatcher {
            return MockUserMatcher(expected)
        }
    }

}
