package com.klinker.android.twitter_l.data.roomdb;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "users", indices = { @Index(value= "screen_name", unique = true)} )
public class User {

    @PrimaryKey
    public long id;

    @ColumnInfo
    public String name;

    @ColumnInfo(name = "screen_name")
    public String screenName;

    @ColumnInfo(name = "profile_pic")
    public String profilePic;

    @ColumnInfo(name = "is_verified")
    public boolean isVerified;


    public User(twitter4j.User user) {

        id = user.getId();
        name = user.getName();
        screenName = user.getScreenName();
        isVerified = user.isVerified();

        String profilePicUrl = user.getOriginalProfileImageURLHttps();

        if (profilePicUrl == null || profilePicUrl.isEmpty()) {
            profilePicUrl = user.getOriginalProfileImageURL();
        }

        profilePic = profilePicUrl;

    }


    public User(long id, String name, String screenName, String profilePic, boolean isVerified) {
        this.id = id;
        this.name = name;
        this.screenName = screenName;
        this.profilePic = profilePic;
        this.isVerified = isVerified;
    }


}
