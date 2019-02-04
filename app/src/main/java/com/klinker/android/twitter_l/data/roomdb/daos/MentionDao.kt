package com.klinker.android.twitter_l.data.roomdb.daos


import android.content.SharedPreferences
import com.klinker.android.twitter_l.data.roomdb.entities.Mention

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.klinker.android.twitter_l.data.roomdb.pojos.DisplayMention


@Dao
abstract class MentionDao {

    internal data class MentionFilterProperties(val text: String, val hashtags: String?)

    @Insert
    abstract fun insertMention(mention: Mention)

    @Insert
    abstract fun insertMentions(mentions: List<Mention>)


    //---------------------------------------------------------------------------
    //Queries with mutes

    @Query("SELECT text, hashtags FROM mentions JOIN tweets ON mentions.tweet_id = tweets.id AND account = :account AND is_unread = 1 JOIN users ON users.id = tweets.user_id AND lower(screen_name) IN(:mutedScreenNames)")
    internal abstract fun getFilteredUnreadCount(account: Int, mutedScreenNames: List<String>) : List<MentionFilterProperties>

    private fun getUnreadCountWithMutes(account: Int, mutedScreenNames: List<String>, mutedRegexes: List<String>, mutedHashtags: List<String>) : Int {
        val filteredMentions = getFilteredUnreadCount(account, mutedScreenNames)

        return filteredMentions.count { mention ->
            mention.text.findAnyOf(mutedRegexes) == null
            && mention.hashtags?.findAnyOf(mutedHashtags) == null
        }

    }



    //----------------------------------------------------------------------------
    //Queries without mutes

    @Query("SELECT COUNT(id) FROM mentions WHERE account = :account AND is_unread = 1")
    internal abstract fun getUnreadCountWithoutMutes(account: Int) : Int


    @Query("SELECT picture_urls FROM mentions JOIN tweets ON mentions.tweet_id = tweets.id AND account = :account ORDER BY tweet_id DESC LIMIT 1")
    internal abstract fun getLatestPictureUrlsWithoutMutes(account: Int) : String?


    @Query("SELECT text FROM mentions JOIN tweets ON mentions.tweet_id = tweets.id AND account = :account ORDER BY tweet_id DESC LIMIT 1")
    internal abstract fun getNewestMessageWithoutMutes(account: Int): String?


    @Query("SELECT users FROM mentions JOIN tweets ON mentions.tweet_id = tweets.id AND account = :account ORDER BY tweet_id DESC LIMIT 1")
    internal abstract fun getNewestUsersWithoutMutes(account: Int) : String?


    //only returns id, text, picture_urls, screen_name, and text
//    @Query("SELECT mentions.id, users, text, picture_urls, screen_name FROM mentions JOIN tweets ON mentions.tweet_id = tweets.id AND account = :account JOIN users ON users.id = tweets.user_id ORDER BY tweet_id DESC LIMIT 1")
//    internal abstract fun getNewestMention(account: Int) : DisplayMention?


    //-----------------------------------------------------------------------------





    //TODO: see if removeHTML is a necessary DAO function

    @Query("DELETE FROM mentions WHERE account = :account")
    abstract fun deleteAllMentions(account: Int)


    //TODO: see if markRead should need both account and tweetId, then implement and test it

    @Query("UPDATE mentions SET is_unread = 0 WHERE account = :account AND is_unread = 1")
    abstract fun markAllRead(account: Int)


    @Query("DELETE FROM mentions WHERE account = :account AND id <= (SELECT id FROM mentions WHERE account = :account ORDER BY id DESC LIMIT 1 OFFSET :trimSize)")
    abstract fun trimDatabase(account: Int, trimSize: Int)


    @Query("SELECT tweet_id FROM mentions WHERE account = :account ORDER BY id DESC LIMIT :limit")
    abstract fun getLatestTweetIds(account: Int, limit: Int) : List<Long>


    fun getUnreadCount(account: Int, includeMutedMentions: Boolean = false) : Int {
        return if (includeMutedMentions) {
            getUnreadCountWithoutMutes(account)
        } else {
            -1 //TODO: add get unread count with mutes function
        }
    }

    fun getUnreadCount(account: Int, sharedPreferences: SharedPreferences) : Int {
        val showMutedMentions = sharedPreferences.getBoolean("show_muted_mentions", false)

        return if (showMutedMentions) {
            getUnreadCountWithoutMutes(account)

        } else {
            val mutedScreenNames = sharedPreferences.getString("muted_users", "")?.split(" ") ?: emptyList()
            val mutedHashtags = sharedPreferences.getString("muted_hashtags", "")?.split(" ") ?: emptyList()
            val mutedExpressions = sharedPreferences.getString("muted_regex", "")?.replace("'", "''")?.split("   ")
                    ?: emptyList()

            getUnreadCountWithMutes(account, mutedScreenNames, mutedExpressions, mutedHashtags)
        }


    }

    fun getLatestDisplayMention(account: Int, sharedPreferences: SharedPreferences) : DisplayMention? {
        return null
    }


    fun getLatestPictureUrls(account: Int, includeMutedMentions: Boolean = false) : String? {
        return if (includeMutedMentions) {
            getLatestPictureUrlsWithoutMutes(account)
        } else {
            null
        }
    }

}
