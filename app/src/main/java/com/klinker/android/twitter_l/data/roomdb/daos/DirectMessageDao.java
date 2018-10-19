package com.klinker.android.twitter_l.data.roomdb.daos;


import com.klinker.android.twitter_l.data.roomdb.entities.DirectMessage;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

@Dao
public abstract class DirectMessageDao {


    @Insert
    abstract void insertDirectMessageSpecificInfo(DirectMessage directMessage);

    @Query("SELECT * FROM direct_messages WHERE account = :account")
    abstract List<DirectMessage> getDirectMessages(int account);

}
