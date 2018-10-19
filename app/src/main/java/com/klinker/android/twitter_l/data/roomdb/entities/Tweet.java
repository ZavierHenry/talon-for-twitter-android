package com.klinker.android.twitter_l.data.roomdb.entities;


import org.jetbrains.annotations.NotNull;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import twitter4j.Status;


@Entity(tableName = "tweets",
foreignKeys = { @ForeignKey(entity = User.class, parentColumns = "id", childColumns = "user_id", onDelete = ForeignKey.CASCADE)})
public class Tweet {

    @PrimaryKey
    public long id;

    @ColumnInfo
    @NotNull
    public String text;

    @ColumnInfo(name = "user_id")
    public long userId;

    @ColumnInfo
    public long time;

    @ColumnInfo
    public String urls;

    @ColumnInfo
    public String users;

    @ColumnInfo(name = "picture_urls")
    public String pictureUrls;

    @ColumnInfo
    public String retweeter;

    @ColumnInfo(name = "gif_url")
    public String gifUrl;

    @ColumnInfo(name = "is_conversation")
    public boolean isConversation;

    @ColumnInfo(name = "media_length")
    public int mediaLength;

    @ColumnInfo(name = "like_count")
    public int likeCount;

    @ColumnInfo(name = "retweet_count")
    public int retweetCount;

    @ColumnInfo(name="client_source")
    public String clientSource;

    @ColumnInfo
    public String hashtags;

    public Tweet() {

    }


    public Tweet(Status status) {

        this.id = status.getId();
        this.time = status.getCreatedAt().getTime();
        this.retweeter = "";


        if (status.isRetweet()) {
            this.retweeter = status.getUser().getScreenName();
            status = status.getRetweetedStatus();
        }


        this.likeCount = status.getFavoriteCount();
        this.retweetCount = status.getRetweetCount();


    }

}

