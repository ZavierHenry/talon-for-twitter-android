package com.klinker.android.twitter_l.data.roomdb;

import android.content.Context;

import com.klinker.android.twitter_l.R;

import twitter4j.Status;

public class QuotedTweetInteraction extends Interaction {

    private static final int INTERACTION_TYPE = 5;

    public QuotedTweetInteraction(Context context, Status status, int account) {
        super(status, account, "<b>@" + status.getUser().getScreenName() + "</b> " + context.getResources().getString(R.string.quoted), INTERACTION_TYPE);
    }

}
