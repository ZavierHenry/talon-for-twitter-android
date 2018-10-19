package com.klinker.android.twitter_l.data.roomdb.pojos;

import org.jetbrains.annotations.NotNull;

import androidx.room.Embedded;
import com.klinker.android.twitter_l.data.roomdb.entities.Tweet;
import com.klinker.android.twitter_l.data.roomdb.entities.User;


public class TweetWithUserInfo {

    @Embedded
    public Tweet tweet;

    @Embedded
    public User user;

}
