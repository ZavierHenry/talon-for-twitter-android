package com.klinker.android.twitter_l.data.roomdb;


import android.util.Log;

import com.klinker.android.twitter_l.utils.TweetLinkUtils;

import java.util.List;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import twitter4j.MediaEntity;
import twitter4j.URLEntity;



@Entity(tableName = "direct_messages",
        indices = { @Index(value="sender_id"), @Index(value = "recipient_id")},
        foreignKeys =
                {
                        @ForeignKey(entity = User.class, parentColumns = "id", childColumns = "sender_id", onDelete = ForeignKey.CASCADE),
                        @ForeignKey(entity = User.class, parentColumns = "id", childColumns = "recipient_id")
                })
public class DirectMessage {

    @PrimaryKey
    public long id;

    @ColumnInfo
    public int account;

    @ColumnInfo(name = "sender_id")
    public long senderId;

    @ColumnInfo(name = "recipient_id")
    public long recipientId;

    @ColumnInfo
    public long time;

    @ColumnInfo
    public String text;

    @ColumnInfo(name = "pic_urls")
    public String picUrls;

    @ColumnInfo
    public String urls;

    @ColumnInfo(name = "gif_url")
    public String gifUrl;

    @ColumnInfo(name = "media_length")
    public long mediaLength;


    public DirectMessage() {

    }


    public DirectMessage(twitter4j.DirectMessageEvent status, List<twitter4j.User> possibleUsers, int account) {

        String[] html = TweetLinkUtils.getLinksInStatus(status);
        String text = html[0];
        String media = html[1];
        String url = html[2];

        twitter4j.User sender = null, receiver = null;

        for (twitter4j.User user : possibleUsers) {

            if (user.getId() == status.getSenderId()) {
                sender = user;
                if (receiver != null) break;

            } else if (user.getId() == status.getRecipientId()) {
                receiver = user;
                if (sender != null) break;
            }
        }

        if (sender == null || receiver == null) {
            return;
        }

        this.account = account;
        this.text = text;
        this.id = status.getId();
        this.senderId = sender.getId();
        this.time = status.getCreatedTimestamp().getTime();
        this.picUrls = media;

        TweetLinkUtils.TweetMediaInformation info = TweetLinkUtils.getGIFUrl(status.getMediaEntities(), url);
        this.gifUrl = info.url;
        this.mediaLength = info.duration;

        MediaEntity[] entities = status.getMediaEntities();

        if (entities != null && entities.length > 0) {
            this.picUrls = entities[0].getMediaURL();
        }

        //only saves last url?
        //TODO: check if saving only last url is intended behavior
        URLEntity[] urls = status.getUrlEntities();
        for (URLEntity u : urls) {
            Log.v("inserting_dm", "url here: " + u.getExpandedURL());
            this.urls = u.getExpandedURL();
        }

    }


}
