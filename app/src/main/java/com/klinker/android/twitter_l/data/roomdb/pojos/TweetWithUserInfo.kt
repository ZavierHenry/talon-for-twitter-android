package com.klinker.android.twitter_l.data.roomdb.pojos

import androidx.room.Embedded
import com.klinker.android.twitter_l.data.roomdb.entities.Tweet
import com.klinker.android.twitter_l.data.roomdb.entities.User


class TweetWithUserInfo {

    @Embedded
    var tweet: Tweet? = null

    @Embedded
    var user: User? = null

}
