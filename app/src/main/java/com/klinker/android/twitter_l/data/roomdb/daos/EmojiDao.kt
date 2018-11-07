package com.klinker.android.twitter_l.data.roomdb.daos


import com.klinker.android.twitter_l.data.roomdb.entities.Emoji

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface EmojiDao {


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertEmoji(emoji: Emoji)


    @Delete
    fun deleteEmoji(emoji: Emoji)


    @Query("DELETE FROM emojis WHERE id = :id")
    fun deleteEmoji(id: Long)


    @Query("SELECT * FROM emojis ORDER BY count DESC LIMIT :limit")
    fun getRecents(limit: Int): List<Emoji>


    @Query("DELETE FROM emojis WHERE count < (SELECT MIN(count) FROM emojis ORDER BY count DESC LIMIT :offset)")
    fun deleteLeastUsedRecents(offset: Int)


}
