package com.klinker.android.twitter_l.data.roomdb.entities;

import android.content.Context;

import com.klinker.android.twitter_l.R;

import org.jetbrains.annotations.NotNull;

import java.util.GregorianCalendar;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import twitter4j.Status;

@Entity(tableName = "interactions",
        foreignKeys = { @ForeignKey(entity = User.class, parentColumns = "id", childColumns = "interactor_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE )},
        indices = { @Index(value = "time")})
public class Interaction {

    @PrimaryKey(autoGenerate = true)
    public Long id;

    @ColumnInfo(name = "tweet_id")
    public Long tweetId;

    @ColumnInfo(name = "is_unread")
    public boolean isUnread;

    @ColumnInfo
    public int account;

    @ColumnInfo(name = "interactor_id")
    public long interactorUserId;

    @ColumnInfo
    public long time;

    @ColumnInfo(name = "type")
    public int interactionType;

    @ColumnInfo
    @NotNull
    public String users;

    @ColumnInfo
    public String title;

    public Interaction() {

    }

    public Interaction(Status status, int account, String title, int interactionType) {

        this.account = account;
        this.isUnread = true;
        this.tweetId = status.getId();
        this.interactionType = interactionType;
        this.time = new GregorianCalendar().getTime().getTime();
        this.interactorUserId = status.getUser().getId();
        this.users = "@" + status.getUser().getScreenName();
        this.title = title;
        this.interactionType = interactionType;

    }

    public Interaction(Context context, twitter4j.User follower, int account) {

        this.tweetId = null;
        this.users = "@" + follower.getScreenName();
        this.time = new GregorianCalendar().getTime().getTime();
        this.interactorUserId = follower.getId();
        this.title = "<b>@" + follower.getScreenName()+ "</b> " + context.getResources().getString(R.string.followed);
    }

    public void updateInteraction(Context context, twitter4j.User source, Status status, int account) {
        this.users += " @" + source.getScreenName();
    }

}
