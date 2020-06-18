package com.klinker.android.twitter_l.mockentities

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

        fun<T> getNullableField(cursor: Cursor, columnName: String, getColumnValue: (Int) -> T) : T? {
            val index = cursor.getColumnIndex(columnName)
            return if (cursor.isNull(index)) null else getColumnValue(index)
        }

        fun cursorToUser(cursor: Cursor, prefix: String = "") : User {
            val screenName = cursor.getString(cursor.getColumnIndex("${prefix}screen_name"))
            val name = getNullableField(cursor, "${prefix}name") { cursor.getString(it) }
            val profilePic = getNullableField(cursor, "${prefix}profile_pic") { cursor.getString(it) }
            val userId = getNullableField(cursor, "${prefix}user_id") { cursor.getLong(it) }
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

            val likes = getNullableField(cursor, "${prefix}likes") { cursor.getInt(it) }
            val retweets = getNullableField(cursor, "${prefix}retweets") { cursor.getInt(it) }
            val liked = getNullableField(cursor, "${prefix}liked") { cursor.getInt(it) == 1 }
            val retweeted = getNullableField(cursor, "${prefix}retweeted") { cursor.getInt(it) == 1 }
            val source = getNullableField(cursor, "${prefix}source") { cursor.getString(it) }
            val gifUrl = getNullableField(cursor, "${prefix}gif_url") { cursor.getString(it) }
            val mediaLength = getNullableField(cursor, "${prefix}media_length") { cursor.getLong(it) }

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
                    liked,
                    retweeted,
                    source,
                    if (retweeter.screenName == "") null else retweeter,
                    gifUrl,
                    mediaLength
            )
        }

        fun makeMockUser(
                screenName: String = "chrislhayes",
                name: String? = "Chris Hayes",
                profilePic: String? = "Image_Avatar.jpg",
                userId: Long = 43298L
        ) : User {
            return User(screenName, name, profilePic, userId)
        }

        fun makeMockTweet(
                tweetId: Long = 1L,
                author: User = makeMockUser(),
                text: String = "",
                time: Long = 0L,
                hashtags: List<String> = List(0) { "" },
                mentions: List<String> = List(0) { "" },
                images: List<String> = List(0) { "" },
                urls: List<String> = List(0) { "" },
                likes: Int? = 0,
                retweets: Int? = 0,
                liked: Boolean? = false,
                retweeted: Boolean? = false,
                source: String? = null,
                retweeter: User? = null,
                gifUrl: String? = null,
                mediaLength: Long? = null
        ) : Tweet {
            return Tweet(tweetId, author, text, time, hashtags, mentions, images, urls, likes, retweets, liked, retweeted, source, retweeter, gifUrl, mediaLength)
        }

    }
}