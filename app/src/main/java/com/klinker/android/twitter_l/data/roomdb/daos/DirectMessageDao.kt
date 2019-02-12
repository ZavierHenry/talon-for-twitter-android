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



    @Query("SELECT * FROM direct_messages WHERE account = :account ORDER BY message_id DESC LIMIT :pageSize OFFSET ((:page - 1) * :pageSize)")
    abstract fun getAllDirectMessages(account: Int, page: Int = 1, pageSize: Int = 100) : List<DisplayDirectMessage>


    @Query("SELECT * FROM direct_messages WHERE account = :account AND (sender_screen_name = :screenName OR recipient_screen_name = :screenName) ORDER BY message_id DESC LIMIT :pageSize OFFSET ((:page - 1) * :pageSize)")
    abstract fun getConversation(screenName: String, account: Int, page: Int = 1, pageSize: Int = 100) : List<DisplayDirectMessage>


    @Query("SELECT * FROM direct_messages WHERE account = :account AND (sender_user_twitter_id = :userId OR recipient_user_twitter_id = :userId) ORDER BY message_id DESC LIMIT :pageSize OFFSET ((:page - 1) * :pageSize)")
    abstract fun getConversation(userId: Long, account: Int, page: Int = 1, pageSize: Int = 100) : List<DisplayDirectMessage>


    //TODO: get direct messages

    //TODO: delete direct message by message id
    @Query("DELETE FROM direct_messages WHERE message_id = :messageId")
    abstract fun deleteDirectMessages(messageId: Long)


    @Query("DELETE FROM direct_messages WHERE account = :account")
    abstract fun deleteAllDirectMessages(account: Int)



    @Query("SELECT * FROM direct_messages WHERE account = :account AND sender_user_twitter_id = :userId ORDER BY message_id DESC LIMIT 1")
    abstract fun getLatestDirectMessage(userId: Long, account: Int) : DisplayDirectMessage?


    //TODO: check whether "removeHTML" is a necessary function

    @Query("DELETE FROM direct_messages WHERE account = :account AND message_id <= (SELECT message_id FROM direct_messages WHERE account = :account ORDER BY message_id DESC LIMIT 1 OFFSET :trimSize)")
    abstract fun trimDatabase(account: Int, trimSize: Int)

}
