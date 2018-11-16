package com.klinker.android.twitter_l.data.roomdb.daos


import com.klinker.android.twitter_l.data.roomdb.entities.Mention

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update


@Dao
abstract class MentionDao : TweetDao() {

    @Insert
    internal abstract fun insertMention(mention: Mention)


    @Query("SELECT tweet_id FROM mentions WHERE account = :account ORDER BY tweet_id DESC LIMIT 2")
    internal abstract fun getLastestTwoIds(account: Int): LongArray


    @Query("DELETE FROM mentions WHERE account = :account AND id < (SELECT MIN(id) from mentions WHERE account = :account ORDER BY id DESC LIMIT :trimSize)")
    abstract fun trimDatabase(account: Int, trimSize: Int)

    @Update
    internal abstract fun updateMention(mention: Mention)

    @Query("UPDATE mentions SET is_unread = 0 WHERE tweet_id = :tweetId AND account = :account")
    internal abstract fun markRead(tweetId: Long, account: Int)


}
