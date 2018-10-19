package com.klinker.android.twitter_l.data.roomdb.entities;

import android.content.Context;

import com.klinker.android.twitter_l.R;

import twitter4j.Status;

public class LikeInteraction extends Interaction {

    private static int INTERACTION_TYPE = 2;
    private int likes;


    public LikeInteraction(Context context, Status status, int account) {
        super(status, account, "<b>@" + status.getUser().getScreenName() + "</b> " + context.getResources().getString(R.string.favorited), INTERACTION_TYPE);
        this.likes = 1;
    }

}
