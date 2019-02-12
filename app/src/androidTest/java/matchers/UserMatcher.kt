package matchers

import com.klinker.android.twitter_l.data.roomdb.entities.User

class UserMatcher private constructor(expected: User) : EntityMatcher<User>(expected) {


    override fun getMismatches(actual: User): Map<String, Mismatch> {
        return mapOf(
                "twitterId" to makeMismatch(expected.userId, actual.userId),
                "name" to makeMismatch(expected.name, actual.name),
                "screenName" to makeMismatch(expected.screenName, actual.screenName),
                "profilePic" to makeMismatch(expected.profilePic, actual.profilePic),
                "isVerified" to makeMismatch(expected.isVerified, actual.isVerified)
        ).filterNot { (_, values) -> values.first == values.second }
    }


    companion object {

        @JvmStatic fun matchesUser(expected: User): UserMatcher {
            return UserMatcher(expected)
        }

    }

}