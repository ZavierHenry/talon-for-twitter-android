package com.klinker.android.twitter_l.data.roomdb.entities;

import android.content.Context;

import com.klinker.android.twitter_l.R;

import twitter4j.Status;

public class MentionInteraction extends Interaction {

    private static int INTERACTION_TYPE = 3;



    public MentionInteraction(Context context, Status status, int account) {
        super(status, account, context.getResources().getString(R.string.mentioned_by) + "<b>@" + status.getUser().getScreenName() + "</b>", INTERACTION_TYPE);
    }


}
