package com.klinker.android.twitter_l.data.roomdb.entities


import android.content.Context
import android.text.TextUtils

import com.klinker.android.twitter_l.R

import org.w3c.dom.Text

import java.util.ArrayList
import java.util.Calendar
import java.util.Collections
import java.util.HashSet

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "Activities",
        foreignKeys = [
            ForeignKey(entity = TweetInteraction::class, childColumns = ["tweet_id"], parentColumns = ["id"], onDelete = ForeignKey.CASCADE),
            ForeignKey(entity = UserInteraction::class, childColumns = ["user_id"], parentColumns = ["id"], onDelete = ForeignKey.CASCADE)])
data class Activity(@PrimaryKey(autoGenerate = true) val id: Long? = null,
                    val type: Int,
                    @ColumnInfo val account: Int = -1,
                    @ColumnInfo(name = "tweet_id") val tweetId: Long? = null,
                    @ColumnInfo(name = "user_id") val userId: Long? = null,
                    val time: Long = 0,
                    val text: String = "",
                    val profilePicUrls: String? = null,
                    val title: String? = null,
                    val users: String = "") {



    companion object {

        const val MENTION = 0
        const val NEW_FOLLOWER = 1
        const val RETWEETS = 2
        const val FAVORITES = 3

        @JvmStatic private fun buildText(context: Context, tweet: Tweet, activityType: Int) : String {
            return when (activityType) {
                MENTION -> tweet.text
                RETWEETS -> "${tweet.retweetCount} ${context.getString(if (tweet.retweetCount == 1) R.string.retweet else R.string.retweets)}"
                FAVORITES -> "${tweet.likeCount} ${context.getString(if (tweet.likeCount == 1) R.string.favorite_lower else R.string.favorites_lower)}"
                else -> ""
            }
        }

        @JvmStatic private fun buildText(context: Context, users: List<User>) : String {
            val size = users.size
            return "$size ${context.getString(if (size == 1) R.string.new_follower_lower else R.string.new_followers_lower)}"
        }






        @JvmStatic fun buildScreenNamesList(users : List<User>) : String {
            return users.asSequence()
                    .distinctBy { it.screenName }
                    .joinToString(separator = " ", transform = { user -> "@${user.screenName}" })
        }

        @JvmStatic fun buildUsersList(context: Context, users: List<User>) : String {
            val and = context.getString(R.string.and)

            return when (users.size) {
                0 -> ""
                1 -> "@${users[0].screenName}"
                2 -> "@${users[0].screenName} $and @${users[1].screenName}"
                else ->
                    users.dropLast(1).joinToString(separator = " ", postfix = ", $and @${users.last().screenName}", transform = { user -> "@${user.screenName}"})
            }
        }

        @JvmStatic fun buildProPicUrl(users: List<User>) : String {
            return users.joinToString(separator = " ", limit = 4, transform = { user -> user.profilePic})
        }

    }

}