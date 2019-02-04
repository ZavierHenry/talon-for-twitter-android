package com.klinker.android.twitter_l.data.roomdb.pojos

import androidx.room.ColumnInfo
import androidx.room.Embedded
import com.klinker.android.twitter_l.data.roomdb.entities.*


abstract class SavedEntity {
    var changed : Boolean = false
}




data class DisplayTweet(@ColumnInfo(name = "tweet_id") val tweetId: Long,
                        @ColumnInfo(name = "tweet_interaction_id") val tweetInteractionId: Long,
                        val text: String,
                        val time: Long = 0,
                        val urls: String? = null,
                        val users: String? = null,
                        @ColumnInfo(name = "picture_urls") val pictureUrls: String? = null,
                        val retweeter: String? = null,
                        @ColumnInfo(name = "gif_url") val gifUrl: String? = null,
                        @ColumnInfo(name = "is_conversation") val isConversation: Boolean? = null,
                        @ColumnInfo(name = "media_length") val mediaLength: Int? = null,
                        @ColumnInfo(name = "like_count") val likeCount: Int? = null,
                        @ColumnInfo(name = "retweet_count") val retweetCount: Int? = null,
                        @ColumnInfo(name = "client_source") val clientSource: String? = null,
                        val hashtags: String? = null,
                        @ColumnInfo(name = "is_liked") val isLiked: Boolean = false,
                        @ColumnInfo(name = "is_retweeted") val isRetweeted: Boolean = false,
                        @ColumnInfo(name = "is_unread") val isUnread: Boolean = false,
                        @Embedded val user: User)


data class DisplayUser(val id: Long? = null,
                       @Embedded(prefix = "user_") val user: User)




data class DisplayFavoriteUser(@Embedded(prefix = "favorite_") val favoriteUser: FavoriteUser,
                               @Embedded(prefix = "user_") val user: User)


data class DisplayFollower(val followerId: Long? = null,
                           @Embedded(prefix = "user_") val user: User)


data class DisplayMention(val mentionId: Long? = null,
                          val isUnread: Boolean = false,
                          @Embedded(prefix = "tweet_") val tweet: DisplayTweet)


data class DisplayFavoriteTweet(val favoriteTweetId: Long? = null,
                                @Embedded(prefix = "tweet_") val tweet: DisplayTweet) {

    fun toFavoriteTweet(account: Int) : FavoriteTweet {
        return FavoriteTweet(favoriteTweetId, account, tweet.tweetId, tweet.isRetweeted, tweet.isUnread)
    }
}


data class DisplayDirectMessage(@ColumnInfo(name = "id") val directMessageId: Long? = null,
                                @Embedded(prefix = "sender_") val sender: User,
                                @Embedded(prefix = "recipient_") val recipient: User,
                                @ColumnInfo(name = "message_id") val messageId: Long = 0,
                                val time: Long,
                                val text: String,
                                @ColumnInfo(name = "picture_urls") val pictureUrls: String? = null,
                                val urls: String? = null,
                                @ColumnInfo(name = "gif_url") val gifUrl: String? = null,
                                @ColumnInfo(name = "media_length") val mediaLength: Long = -1) {

    constructor(directMessage: DirectMessage, sender: User, recipient: User) : this(
            directMessage.id,
            sender,
            recipient,
            directMessage.messageId,
            directMessage.time,
            directMessage.text,
            directMessage.pictureUrls,
            directMessage.urls,
            directMessage.gifUrl,
            directMessage.mediaLength
    )

}
