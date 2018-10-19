package com.klinker.android.twitter_l.data.roomdb;


import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface DraftDao {

    @Insert
    void insertDraft(Draft draft);

    @Delete
    void deleteDraft(Draft draft);

    @Query("DELETE FROM drafts WHERE text = :message")
    void deleteDraft(String message);


    @Query("SELECT * FROM drafts WHERE account = :account")
    List<Draft> getDrafts(int account);


}
