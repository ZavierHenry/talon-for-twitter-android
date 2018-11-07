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
import androidx.room.PrimaryKey
import twitter4j.Status
import twitter4j.User


//TODO: add foreign keys for tweet_id and user_id
@Entity(tableName = "activities")
class Activity() {

    // MENTION = 0
    // NEW_FOLLOWER = 1
    // RETWEETS = 2
    // FAVORITES = 3

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null

    @ColumnInfo
    var account: Int = 0

    @ColumnInfo(name = "tweet_id")
    var tweetId: Long? = null

    @ColumnInfo
    var time: Long = 0

    @ColumnInfo
    var text: String = ""

    @ColumnInfo(name = "profile_pic_urls")
    var proPicUrls: String? = null

    @ColumnInfo
    var title: String? = null

    @ColumnInfo(name = "user_id")
    var userId: Long? = null


    @ColumnInfo
    var type: Int = 0 //change to type ActivityType enum

    init {

    }

    constructor(status: Status, account: Int, activityType: Int) : this() {
        this.id = null
        this.account = account
        this.type = activityType

        when (activityType) {
            0 -> text = ""
            1 -> text = ""
            else -> text = ""
        }
    }


    constructor(context: Context, users: List<User>, account: Int) : this() {
        this.id = null
        this.account = account
        this.tweetId = null
        this.type = 1 //new follower
        this.text = "${users.size} ${context.getString(if (users.size == 1) R.string.new_follower_lower else R.string.new_followers_lower)}}"
        this.userId = null
        this.time = Calendar.getInstance().timeInMillis


    }

    private fun buildProPicUrl(users: List<User>): String {
        return users.asSequence().take(4).joinToString(separator = " "){ user -> "@${user.screenName}" }
    }

    private fun buildUserList(users: List<User>): String {
        return users.asSequence().distinctBy { user -> user.screenName }.joinToString(separator = " ") { user -> "@${user.screenName}" }
    }

    private fun buildUsersTitle(context: Context, users: List<User>): String {
        val and = context.getString(R.string.and)
        val screenNames = ArrayList<String>()

        return when (users.size) {
            0 -> ""
            1 -> "@${users[0].screenName}"
            2 -> "@${users[0].screenName} $and ${users[1].screenName}"
            else -> users.asSequence().take(users.size - 1).joinToString(separator = ", ") { user -> "@${user.screenName}"} + ", $and @" + users.last().screenName
        }
    }

}