package com.klinker.android.twitter_l.data.roomdb;


import org.jetbrains.annotations.NotNull;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "drafts", indices = { @Index(value = "text")})
public class Draft {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "draft_id")
    public final Integer id;

    @ColumnInfo
    @NotNull
    public String text;

    @ColumnInfo
    public int account;

    @Ignore
    public Draft(@NotNull String text, int account) {
        this.id = null;
        this.text = text;
        this.account = account;
    }

    public Draft(int id, @NotNull String text, int account) {
        this.id = id;
        this.text = text;
        this.account = account;
    }

}
