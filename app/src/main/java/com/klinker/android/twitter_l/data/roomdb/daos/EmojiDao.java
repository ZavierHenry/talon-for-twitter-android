package com.klinker.android.twitter_l.data.roomdb.daos;


import com.klinker.android.twitter_l.data.roomdb.entities.Emoji;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface EmojiDao {


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertEmoji(Emoji emoji);


    @Delete
    void deleteEmoji(Emoji emoji);


    @Query("DELETE FROM emojis WHERE id = :id")
    void deleteEmoji(long id);


    @Query("SELECT * FROM emojis ORDER BY count DESC LIMIT :limit")
    List<Emoji> getRecents(int limit);


    @Query("DELETE FROM emojis WHERE count < (SELECT MIN(count) FROM emojis ORDER BY count DESC LIMIT :offset)")
    void deleteLeastUsedRecents(int offset);


}
