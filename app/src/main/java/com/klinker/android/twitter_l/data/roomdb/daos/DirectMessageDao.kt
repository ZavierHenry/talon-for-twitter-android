package com.klinker.android.twitter_l.data.roomdb.daos


import androidx.room.*
import com.klinker.android.twitter_l.data.roomdb.entities.DirectMessage
import com.klinker.android.twitter_l.data.roomdb.pojos.DisplayDirectMessage
import com.klinker.android.twitter_l.data.roomdb.pojos.DisplayTweet

@Dao
abstract class DirectMessageDao {



    //TODO: insert direct message



    //TODO: internal direct message
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    internal abstract fun insertDirectMessage(directMessage: DirectMessage): Long?



    @Query("SELECT direct_messages.id as id, message_id, time, text, picture_urls, urls, gif_url, media_length, " +
            "sender.id as sender_id, sender.twitter_id as sender_twitter_id, sender.name as sender_name, sender.screen_name as sender_screen_name, sender.profile_pic as sender_profile_pic, sender.is_verified as sender_is_verified, " +
            "recipient.id as recipient_id, recipient.twitter_id as recipient_twitter_id, recipient.name as recipient_name, recipient.screen_name as recipient_screen_name, recipient.profile_pic as recipient_profile_pic, recipient.is_verified as recipient_is_verified " +
            "FROM direct_messages JOIN users sender ON direct_messages.sender_id = sender.id AND account = :account " +
            "JOIN users recipient ON direct_messages.recipient_id = recipient.id " +
            "GROUP BY message_id ORDER BY message_id ASC LIMIT :pageSize OFFSET ((:page - 1) * :pageSize)")
    abstract fun getAllDirectMessages(account: Int, page: Int = 1, pageSize: Int = 100) : List<DisplayDirectMessage>


    @Query("SELECT direct_messages.id as id, message_id, time, text, picture_urls, urls, gif_url, media_length, " +
            "sender.id as sender_id, sender.twitter_id as sender_twitter_id, sender.name as sender_name, sender.screen_name as sender_screen_name, sender.profile_pic as sender_profile_pic, sender.is_verified as sender_is_verified, " +
            "recipient.id as recipient_id, recipient.twitter_id as recipient_twitter_id, recipient.name as recipient_name, recipient.screen_name as recipient_screen_name, recipient.profile_pic as recipient_profile_pic, recipient.is_verified as recipient_is_verified " +
            "FROM direct_messages JOIN users sender ON direct_messages.sender_id = sender.id AND account = :account " +
            "JOIN users recipient ON direct_messages.recipient_id = recipient.id " +
            "WHERE sender.screen_name = :screenName OR recipient.screen_name = :screenName " +
            "GROUP BY message_id ORDER BY message_id ASC LIMIT :pageSize OFFSET ((:page - 1) * :pageSize)")
    abstract fun getConversation(screenName: String, account: Int, page: Int = 1, pageSize: Int = 100) : List<DisplayDirectMessage>


    @Query("SELECT direct_messages.id as id, message_id, time, text, picture_urls, urls, gif_url, media_length, " +
            "sender.id as sender_id, sender.twitter_id as sender_twitter_id, sender.name as sender_name, sender.screen_name as sender_screen_name, sender.profile_pic as sender_profile_pic, sender.is_verified as sender_is_verified, " +
            "recipient.id as recipient_id, recipient.twitter_id as recipient_twitter_id, recipient.name as recipient_name, recipient.screen_name as recipient_screen_name, recipient.profile_pic as recipient_profile_pic, recipient.is_verified as recipient_is_verified " +
            "FROM direct_messages JOIN users sender ON direct_messages.sender_id = sender.id AND account = :account AND (direct_messages.sender_id = :userId OR direct_messages.recipient_id = :userId) " +
            "JOIN users recipient ON direct_messages.recipient_id = recipient.id " +
            "GROUP BY message_id ORDER BY message_id ASC LIMIT :pageSize OFFSET ((:page - 1) * :pageSize)")
    abstract fun getConversation(userId: Long, account: Int, page: Int = 1, pageSize: Int = 100) : List<DisplayDirectMessage>


    //TODO: get direct messages

    //TODO: delete direct message by message id
    @Query("DELETE FROM direct_messages WHERE message_id = :messageId")
    abstract fun deleteDirectMessages(messageId: Long)


    @Query("DELETE FROM direct_messages WHERE account = :account")
    abstract fun deleteAllDirectMessages(account: Int)



    @Query("SELECT direct_messages.id as id, message_id, time, text, picture_urls, urls, gif_url, media_length, " +
            "sender.id as sender_id, sender.twitter_id as sender_twitter_id, sender.name as sender_name, sender.screen_name as sender_screen_name, sender.profile_pic as sender_profile_pic, sender.is_verified as sender_is_verified, " +
            "recipient.id as recipient_id, recipient.twitter_id as recipient_twitter_id, recipient.name as recipient_name, recipient.screen_name as recipient_screen_name, recipient.profile_pic as recipient_profile_pic, recipient.is_verified as recipient_is_verified " +
            "FROM direct_messages JOIN users sender ON direct_messages.sender_id = sender.id AND account = :account AND sender_id = :userId " +
            "JOIN users recipient ON direct_messages.recipient_id = recipient.id " +
            "GROUP BY message_id ORDER BY message_id DESC LIMIT 1")
    abstract fun getLatestDirectMessage(userId: Long, account: Int) : DisplayDirectMessage?


    //TODO: check whether "removeHTML" is a necessary function

    @Query("DELETE FROM direct_messages WHERE account = :account AND message_id <= (SELECT message_id FROM direct_messages WHERE account = :account ORDER BY message_id DESC LIMIT 1 OFFSET :trimSize)")
    abstract fun trimDatabase(account: Int, trimSize: Int)

}
