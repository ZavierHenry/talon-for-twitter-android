package com.klinker.android.twitter_l.data.roomdb;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "hashtags", indices = @Index(value= "name", unique = true))
public class Hashtag {

    @PrimaryKey(autoGenerate = true)
    public final Long id;

    @ColumnInfo(name="name")
    public String tag;

    //maybe add account?

    public Hashtag(long id, String tag) {
        this.id = id;
        this.tag = tag;
    }

    @Ignore
    public Hashtag(String tag) {
        this.id = null;
        this.tag = tag;
    }

}
