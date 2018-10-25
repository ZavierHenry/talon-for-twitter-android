package com.klinker.android.twitter_l.data.roomdb.entities;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;



@Entity(tableName = "emojis", indices = {@Index(value = "icon", unique = true)})
public class Emoji {

    @PrimaryKey(autoGenerate = true)
    public final Long id;

    @ColumnInfo
    public String text;

    @ColumnInfo
    public String icon;

    @ColumnInfo
    public int count;

    //maybe add account???

    public Emoji(long id, String text, String icon, int count) {
        this.id = id;
        this.text = text;
        this.icon = icon;
        this.count = count;
    }

    @Ignore
    public Emoji(String text, String icon) {
        this.id = null;
        this.text = text;
        this.icon = icon;
        this.count = 0;
    }


}
