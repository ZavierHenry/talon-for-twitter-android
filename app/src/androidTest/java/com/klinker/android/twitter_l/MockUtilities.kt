package com.klinker.android.twitter_l

import android.content.ContentValues
import android.database.Cursor
import com.klinker.android.twitter_l.data.roomdb.Tweet
import com.klinker.android.twitter_l.data.roomdb.User

class MockUtilities {

    companion object {
        fun userToContentValues(user: User) : ContentValues {
            return ContentValues().apply {
                put("name", user.name)
                put("screen_name", user.screenName)
                put("profile_pic", user.profilePic)
                put("user_id", user.userId)
            }
        }

        fun tweetToContentValues(tweet: Tweet) : ContentValues {
            return ContentValues().apply {
                put("text", tweet.text)
            }
        }

        fun cursorToUser(cursor: Cursor) : User {
            TODO("Not implemented as of yet")
        }

        fun cursorToTweet(cursor: Cursor) : Tweet {
            TODO("Not implemented as of yet")
        }
    }
}