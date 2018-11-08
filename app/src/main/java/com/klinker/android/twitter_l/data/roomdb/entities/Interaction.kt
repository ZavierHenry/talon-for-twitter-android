package com.klinker.android.twitter_l.data.roomdb.entities

import android.content.Context

import com.klinker.android.twitter_l.R

import java.util.GregorianCalendar

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import twitter4j.Status

@Entity(tableName = "interactions",
        foreignKeys = [ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["interactor_id"], onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)],
        indices = [Index(value = ["time"])])
class Interaction() {

    //FOLLOWER = 0
    //RETWEET = 1
    //FAVORITE = 2
    //MENTION = 3
    //FAV_USER = 4
    //QUOTED_TWEET = 5


    @PrimaryKey(autoGenerate = true)
    var id: Long? = null

    @ColumnInfo(name = "tweet_id")
    var tweetId : Long = 0

    @ColumnInfo(name = "is_unread")
    var isUnread: Boolean = false

    @ColumnInfo
    var account: Int = 0

    @ColumnInfo(name = "interactor_id")
    var interactorUserId: Long = 0

    @ColumnInfo
    var time: Long = 0

    @ColumnInfo(name = "type")
    var interactionType: Int = 0

    @ColumnInfo
    var users: String = ""

    @ColumnInfo
    var title: String = ""

    init {

    }


    constructor(context: Context, status: Status?, source: twitter4j.User, account: Int, interactionType: Int) : this() {

        this.account = account
        this.isUnread = true
        this.tweetId = status?.id ?: 0
        this.interactionType = interactionType
        this.time = GregorianCalendar().time.time
        this.interactorUserId = source.id
        this.users = "@" + source.screenName
        this.interactionType = interactionType

        when (interactionType) {

            0 -> {

            }

            1 -> {
                title = "<b>@${source.screenName}</b> ${context.getString(R.string.favorited)}"
                tweetId = status!!.id
            }

            2 -> {
                title = "<b>@${source.screenName}</b> ${context.getString(R.string.retweeted)}"
                tweetId = status!!.id
            }

            3 -> {

            }

            4 -> {

            }

            5 -> {

            }




        }

    }


}
