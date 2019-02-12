package com.klinker.android.twitter_l.data.roomdb.daos


import android.content.SharedPreferences
import com.klinker.android.twitter_l.data.roomdb.entities.Mention

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.klinker.android.twitter_l.data.roomdb.pojos.DisplayMention


@Dao
abstract class MentionDao