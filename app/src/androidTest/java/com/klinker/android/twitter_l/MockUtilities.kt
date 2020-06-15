package com.klinker.android.twitter_l

import android.content.ContentValues
import android.database.Cursor
import com.klinker.android.twitter_l.data.roomdb.ListStringConverter
import com.klinker.android.twitter_l.data.roomdb.Tweet
import com.klinker.android.twitter_l.data.roomdb.User

class MockUtilities {

    companion object {
        fun userToContentValues(user: User, prefix: String = "") : ContentValues {
            return ContentValues().apply {
                put("${prefix}name", user.name)
                put("${prefix}screen_name", user.screenName)
                put("${prefix}profile_pic", user.profilePic)
                put("${prefix}user_id", user.userId)
            }
        }

        fun tweetToContentValues(tweet: Tweet, prefix: String = "") : ContentValues {
            val userContentValues = userToContentValues(tweet.author, "${prefix}user_")
            val converter = ListStringConverter()
            return ContentValues().apply {
                putAll(userContentValues)

                tweet.retweeter?.also {
                    val retweeterContentValues = userToContentValues(it, "${prefix}retweeter_")
                    putAll(retweeterContentValues)
                }

                put("${prefix}tweet_id", tweet.tweetId)
                put("${prefix}text", tweet.text)
                put("${prefix}time", tweet.time)
                put("${prefix}hashtags", converter.fromListString(tweet.hashtags))
                put("${prefix}mentions", converter.fromListString(tweet.mentions))
                put("${prefix}images", converter.fromListString(tweet.images))
                put("${prefix}urls", converter.fromListString(tweet.urls))
                put("${prefix}likes", tweet.likes)
                put("${prefix}retweets", tweet.retweets)
                put("${prefix}liked", tweet.liked)
                put("${prefix}retweeted", tweet.retweeted)
                put("${prefix}source", tweet.source)
                put("${prefix}gif_url", tweet.gifUrl)
                put("${prefix}media_length", tweet.mediaLength)
            }
        }

        fun cursorToUser(cursor: Cursor, prefix: String = "") : User {
            val screenName = cursor.getString(cursor.getColumnIndex("${prefix}screen_name"))
            val name = cursor.getString(cursor.getColumnIndex("${prefix}name"))
            val profilePic = cursor.getString(cursor.getColumnIndex("${prefix}profile_pic"))
            val userId = cursor.getLong(cursor.getColumnIndex("${prefix}user_id"))
            return User(screenName, name, profilePic, userId)
        }

        fun cursorToTweet(cursor: Cursor, prefix: String = "") : Tweet {
            val converter = ListStringConverter()

            val author = cursorToUser(cursor, "${prefix}user_")
            val tweetId = cursor.getLong(cursor.getColumnIndex("${prefix}tweet_id"))
            val text = cursor.getString(cursor.getColumnIndex("${prefix}text"))
            val time = cursor.getLong(cursor.getColumnIndex("${prefix}time"))
            val hashtags = cursor.getString(cursor.getColumnIndex("${prefix}hashtags"))
            val mentions = cursor.getString(cursor.getColumnIndex("${prefix}mentions"))
            val images = cursor.getString(cursor.getColumnIndex("${prefix}images"))
            val urls = cursor.getString(cursor.getColumnIndex("${prefix}urls"))
            val likes = cursor.getInt(cursor.getColumnIndex("${prefix}likes"))
            val retweets = cursor.getInt(cursor.getColumnIndex("${prefix}retweets"))
            val liked = cursor.getInt(cursor.getColumnIndex("${prefix}liked"))
            val retweeted = cursor.getInt(cursor.getColumnIndex("${prefix}retweeted"))
            val source = cursor.getString(cursor.getColumnIndex("${prefix}source"))
            val gifUrl = cursor.getString(cursor.getColumnIndex("${prefix}gif_url"))
            val mediaLength = cursor.getLong(cursor.getColumnIndex("${prefix}media_length"))

            val retweeter = cursorToUser(cursor, "${prefix}retweeter_")

            return Tweet(
                    tweetId,
                    author,
                    text,
                    time,
                    converter.toListString(hashtags),
                    converter.toListString(mentions),
                    converter.toListString(images),
                    converter.toListString(urls),
                    likes,
                    retweets,
                    liked == 1,
                    retweeted == 1,
                    source,
                    if (retweeter.screenName == "") null else retweeter,
                    gifUrl,
                    mediaLength
            )
        }
    }
}