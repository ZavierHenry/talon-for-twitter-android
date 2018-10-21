package com.klinker.android.twitter_l.data.roomdb.entities;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorite_users",
        indices = { @Index(value = { "user_id", "account"}), @Index(value = "account")},
        foreignKeys = { @ForeignKey( entity = User.class, parentColumns = "id", childColumns = "user_id", onDelete = ForeignKey.CASCADE)})
public class FavoriteUser {

    @PrimaryKey
    public long id;

    @ColumnInfo
    public int account;

    @ColumnInfo(name = "user_id")
    public long userId;

}
