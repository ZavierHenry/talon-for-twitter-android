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

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null

    @ColumnInfo(name = "tweet_id")
    var tweetId: Long? = null

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


    constructor(status: Status, account: Int, title: String, interactionType: Int) : this() {

        this.account = account
        this.isUnread = true
        this.tweetId = status.id
        this.interactionType = interactionType
        this.time = GregorianCalendar().time.time
        this.interactorUserId = status.user.id
        this.users = "@" + status.user.screenName
        this.title = title
        this.interactionType = interactionType

    }

    constructor(context: Context, follower: twitter4j.User, account: Int) : this() {

        this.tweetId = null
        this.users = "@" + follower.screenName
        this.time = GregorianCalendar().time.time
        this.interactorUserId = follower.id
        this.title = "<b>@" + follower.screenName + "</b> " + context.resources.getString(R.string.followed)
    }

    fun updateInteraction(context: Context, source: twitter4j.User, status: Status, account: Int) {
        this.users += " @" + source.screenName
    }

}
