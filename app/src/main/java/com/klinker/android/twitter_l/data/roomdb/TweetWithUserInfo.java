package com.klinker.android.twitter_l.data.roomdb;

import org.jetbrains.annotations.NotNull;

import androidx.room.Embedded;

public class TweetWithUserInfo {

    @Embedded
    public Tweet tweet;

    @Embedded
    public User user;

}
