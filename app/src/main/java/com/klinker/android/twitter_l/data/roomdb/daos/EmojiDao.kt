package com.klinker.android.twitter_l.data.roomdb.daos


import androidx.room.*
import com.klinker.android.twitter_l.data.roomdb.entities.Emoji

@Dao
interface EmojiDao {


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertEmoji(emoji: Emoji)

    @Delete
    fun deleteEmoji(emoji: Emoji)


    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateEmoji(emoji: Emoji)


    @Query("SELECT * FROM emojis ORDER BY count DESC LIMIT :limit")
    fun getRecents(limit: Int): List<Emoji>


    @Query("DELETE FROM emojis WHERE count < (SELECT count FROM emojis ORDER BY count DESC LIMIT 1 OFFSET :offset)")
    fun deleteLeastUsedRecents(offset: Int)


}
