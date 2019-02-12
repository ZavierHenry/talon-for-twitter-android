package com.klinker.android.twitter_l.data.roomdb.daos


import android.content.Context
import androidx.room.*
import com.klinker.android.twitter_l.data.roomdb.entities.Tweet

import com.klinker.android.twitter_l.data.roomdb.TalonDatabase
import com.klinker.android.twitter_l.data.roomdb.entities.User

import twitter4j.Status

@Dao
abstract class TweetDao