package com.klinker.android.twitter_l.data.roomdb.entities;

import android.content.Context;

import com.klinker.android.twitter_l.R;

import twitter4j.Status;

public class FavoriteUserInteraction extends Interaction {

    private static int INTERACTION_TYPE = 4;

    public FavoriteUserInteraction(Context context, Status status, int account) {
        super(status, account, "<b>@" + status.getUser().getScreenName() + "</b> " + context.getResources().getString(R.string.tweeted), INTERACTION_TYPE);
    }

}
