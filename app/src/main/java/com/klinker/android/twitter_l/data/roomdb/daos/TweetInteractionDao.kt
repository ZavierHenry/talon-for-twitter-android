package com.klinker.android.twitter_l.data.roomdb.daos

import android.content.SharedPreferences
import androidx.room.*
import com.klinker.android.twitter_l.data.roomdb.entities.Tweet
import com.klinker.android.twitter_l.data.roomdb.entities.TweetInteraction
import com.klinker.android.twitter_l.data.roomdb.pojos.DisplayTweet


@Dao
abstract class TweetInteractionDao {

    @Insert
    abstract fun insertTweetInteraction(interaction: TweetInteraction): Long?

    @Update
    abstract fun updateTweetInteraction(interaction: TweetInteraction): Int



    @Query("UPDATE tweet_interactions SET is_saved = 0 WHERE account = :account")
    abstract fun deleteAllSavedTweets(account: Int)


    @Query("SELECT * FROM tweet_interactions WHERE account = :account AND is_saved = 1 ORDER BY tweet_twitter_id ASC LIMIT :pageSize OFFSET ((:page - 1) * :pageSize)")
    abstract fun getSavedTweets(account: Int, page: Int = 1, pageSize: Int = 1) : List<TweetInteraction>


    @Query("SELECT id FROM tweet_interactions WHERE account = :account AND tweet_twitter_id = :tweetId AND is_saved = 1")
    abstract fun getSavedTweetId(tweetId: Long, account: Int) : Long?


    fun isSavedTweet(tweetId: Long, account: Int) : Boolean {
        return getSavedTweetId(tweetId, account) != null
    }


    //-----------------------------------------------------------------------------------------------------------------------
    //Favorite Tweet functions



    @Query("SELECT * FROM tweet_interactions WHERE account = :account ORDER BY tweet_twitter_id DESC LIMIT :pageSize OFFSET ((:page - 1) * :pageSize)")
    abstract fun getFavoriteTweets(account: Int, page: Int = 1, pageSize: Int = 200) : List<TweetInteraction>


    @Query("SELECT tweet_twitter_id FROM tweet_interactions WHERE account = :account AND is_liked = 1 ORDER BY tweet_twitter_id DESC LIMIT :limit")
    abstract fun getLatestFavoriteTweetIds(account: Int, limit: Int = 5): List<Long>


    @Query("UPDATE tweet_interactions SET is_liked = 0 WHERE account = :account AND is_liked = 1")
    abstract fun deleteAllFavoriteTweets(account: Int)




    //------------------------------------------------------------------------------------------------------------------------
    // Mention functions


    @Query("UPDATE tweet_interactions SET is_mention_unread = 0 WHERE account = :account AND is_mentioned = 1 AND is_mention_unread = 1")
    abstract fun markAllRead(account: Int)

    @Query("SELECT tweet_twitter_id FROM tweet_interactions WHERE account = :account AND is_mentioned = 1 ORDER BY tweet_twitter_id DESC LIMIT :limit")
    abstract fun getLatestMentionIds(account: Int, limit: Int = 2) : List<Long>

    @Query("SELECT * FROM tweet_interactions WHERE account = :account AND is_mentioned = 1 ORDER BY tweet_twitter_id DESC LIMIT :pageSize OFFSET ((:page - 1) * :pageSize)")
    internal abstract fun getAllMentions(account: Int, page: Int = 1, pageSize: Int = 100) : List<TweetInteraction>



    //get latest mention



    @Query("SELECT * FROM tweet_interactions WHERE account = :account AND is_mentioned = 1 AND is_mention_unread = 1 AND lower(tweet_user_screen_name) IN(:mutedUsers)")
    abstract fun getUnreadCountMutedMentions(account: Int, mutedUsers: List<String>) : List<TweetInteraction>


    @Query("SELECT COUNT(1) FROM tweet_interactions WHERE account = :account AND is_mentioned = 1 AND is_mention_unread = 1")
    abstract fun getUnreadCountAllMentions(account: Int) : Int


    fun getUnreadCount(account: Int) : Int {
        return getUnreadCountAllMentions(account)
    }

    fun getUnreadCount(account: Int, mutedUsers: List<String>, mutedHashtags: List<String>, mutedExpressions: List<String>) : Int {
        val tweets = getUnreadCountMutedMentions(account, mutedUsers.map { it.toLowerCase() })
        return tweets.count { tweet ->
            mutedExpressions.none { expression -> tweet.tweet.text.contains(expression, ignoreCase = true) } &&
            tweet.tweet.hashtags.none { hashtag -> mutedHashtags.contains(hashtag.toLowerCase()) }
        }
    }


    //----------------------------------------------------------------------------------------------------------------
    //Home tweets queries



    //abstract fun getDatabaseHomeTweetsWithFilters(account: Int, mutedUsers: List<String>, mutedClients: List<String>, page: Int, pageSize: Int) : List<TweetInteraction>


    fun getHomeTweets(account: Int, mutedUsers: List<String>, mutedRts: List<String>, mutedHashtags: List<String>, mutedExpressions: List<String>, mutedClients: List<String>, page: Int = 1, pageSize: Int = 200) : List<TweetInteraction> {
        return emptyList()
    }


    fun getWidgetTweets(account: Int, mutedUsers: List<String>, mutedRts: List<String>, mutedHashtags: List<String>, mutedExpressions: List<String>, mutedClients: List<String>) : List<TweetInteraction> {
        return getHomeTweets(account, mutedUsers, mutedRts, mutedHashtags, mutedExpressions, mutedClients, page = 1, pageSize = 150)
    }



    fun getUnreadTweets(account: Int, mutedUsers: List<String>, mutedRts: List<String>, mutedHashtags: List<String>, mutedExpressions: List<String>, mutedClients: List<String>, page: Int = 1, pageSize: Int = 200) : List<TweetInteraction> {
        return emptyList()
    }



    fun getPicsTweets(account: Int, mutedUsers: List<String>, mutedRts: List<String>, mutedHashtags: List<String>, mutedExpressions: List<String>, mutedClients: List<String>) : List<TweetInteraction> {
        return emptyList()
    }


    @Query("UPDATE tweet_interactions SET is_home_unread = 0 WHERE account = :account AND is_home_unread = 1")
    abstract fun markAllHomeTweetsRead(account: Int)





    //------------------------------------------------------------------------------------------------------------------------

    @Query("DELETE FROM tweet_interactions WHERE account = :account")
    abstract fun deleteAllTweets(account: Int)


    @Transaction
    open fun trimTweetInteractions(account: Int) {

    }


}