package com.klinker.android.twitter_l.data.roomdb.daos


import android.content.SharedPreferences
import com.klinker.android.twitter_l.data.roomdb.entities.ListTweet

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.klinker.android.twitter_l.data.roomdb.entities.Tweet
import com.klinker.android.twitter_l.data.roomdb.entities.TweetInteraction

@Dao
abstract class ListTweetDao {

    //insert list tweet
    @Insert
    abstract fun insertListTweet(listTweet: ListTweet)

    //insert list tweets
    @Insert
    abstract fun insertListTweets(listTweets: List<ListTweet>)

    @Delete
    abstract fun deleteListTweet(listTweet: ListTweet)

    //TODO: confirm that this should be the behavior of list tweet
    @Query("DELETE FROM list_tweets WHERE tweet_interaction_id = :tweetId")
    abstract fun deleteListTweet(tweetId: Long)

    @Query("DELETE FROM list_tweets WHERE list_id = :listId")
    abstract fun deleteAllTweets(listId: Long)

    //getListCursor equivalent

    fun getListTweets(listId: Long, mutedUsers: List<String>, mutedRts: List<String>, mutedHashtags: List<String>, mutedExpressions: List<String>, noRetweets: Boolean = false, page: Int = 1, pageSize: Int = 200) : List<TweetInteraction> {
       return if (noRetweets) {
           getListTweetsWithNoRetweets(listId, mutedUsers, mutedHashtags, mutedExpressions, page, pageSize)
       } else {
           getListTweetsWithRetweets(listId, mutedUsers, mutedRts, mutedHashtags, mutedExpressions, page, pageSize)
       }
    }


    @Query("SELECT * FROM list_tweets JOIN tweet_interactions AS tweets ON list_tweets.tweet_interaction_id = tweets.id AND list_id = :listId AND lower(tweet_user_screen_name) NOT IN(:mutedUsers) AND (tweet_retweeter = '' OR tweet_retweeter IS NULL) ORDER BY tweet_twitter_id DESC LIMIT :pageSize OFFSET ((:page - 1) * :pageSize)")
    abstract fun queryListTweetsWithNoRetweets(listId: Long, mutedUsers: List<String>, page: Int, pageSize: Int) : List<TweetInteraction>


    @Query("SELECT * FROM list_tweets JOIN tweet_interactions AS tweets ON list_tweets.tweet_interaction_id = tweets.id AND list_id = :listId AND lower(tweet_user_screen_name) NOT IN(:mutedUsers) AND lower(tweet_retweeter) NOT IN (:mutedUsers) AND lower(tweet_retweeter) NOT IN(:mutedRts) ORDER BY tweet_twitter_id DESC LIMIT :pageSize OFFSET ((:page - 1) * :pageSize)")
    abstract fun queryListTweetsWithRetweets(listId: Long, mutedUsers: List<String>, mutedRts: List<String>, page: Int, pageSize: Int) : List<TweetInteraction>


    private fun getListTweetsWithNoRetweets(listId: Long, mutedUsers: List<String>, mutedHashtags: List<String>, mutedExpressions: List<String>, page: Int, pageSize: Int) : List<TweetInteraction> {
        val tweetInteractions = queryListTweetsWithNoRetweets(listId, mutedUsers, page, pageSize)
        return tweetInteractions.filterNot { tweetInteraction ->
            tweetInteraction.tweet.hashtags.any { hashtag -> mutedHashtags.contains(hashtag.toLowerCase()) } ||
                    mutedExpressions.any { expression -> tweetInteraction.tweet.text.toLowerCase().contains(expression) }
        }

    }


    private fun getListTweetsWithRetweets(listId: Long, mutedUsers: List<String>, mutedRts: List<String>, mutedHashtags: List<String>, mutedExpressions: List<String>, page: Int, pageSize: Int) : List<TweetInteraction> {
        val tweetInteractions = queryListTweetsWithRetweets(listId, mutedUsers, mutedRts, page, pageSize)
        return tweetInteractions.filterNot { tweetInteraction ->
            tweetInteraction.tweet.hashtags.any { hashtag -> mutedHashtags.contains(hashtag.toLowerCase()) } ||
                    mutedExpressions.any { expression -> tweetInteraction.tweet.text.toLowerCase().contains(expression) }
        }

    }


    //TODO: confirm getWholeCursor equivalent is unnecessary

    //getLastIds equivalent
    open fun getLastIds(listId: Long, mutedUsers: List<String>, mutedRts: List<String>, mutedHashtags: List<String>, mutedExpressions: List<String>, noRetweets: Boolean = false, limit: Int = 5) : List<Long> {
        return getListTweets(listId, mutedUsers, mutedRts, mutedHashtags, mutedExpressions, noRetweets, 1, limit).map { tweetInteraction -> tweetInteraction.tweet.twitterId }
    }

    open fun getLastIds(listId: Long, preferences: SharedPreferences, limit: Int = 5) : List<Long> {
        val noRetweets = preferences.getBoolean("ignore_retweets", false)
        val users = preferences.getString("muted_users", "")?.split(" ") ?: emptyList()
        val rts = preferences.getString("muted_rts", "")?.split(" ") ?: emptyList()
        val hashtags = preferences.getString("muted_hashtags", "")?.split(" ") ?: emptyList()
        val expressions = preferences.getString("muted_regex", "")?.replace("'", "''")?.split("   ") ?: emptyList()

        return getLastIds(listId, users, rts, hashtags, expressions, noRetweets, limit)

    }

    //TODO: write remove html equivalent

    @Query("DELETE FROM list_tweets WHERE list_id = :listId AND id < (SELECT id FROM list_tweets WHERE list_id = :listId ORDER BY id DESC LIMIT 1 OFFSET :trimSize)")
    abstract fun trimDatabase(listId: Long, trimSize: Int)


}
