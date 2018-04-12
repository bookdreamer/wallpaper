package com.yyablunovska.wallpaper.model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * @author yuliia.yablunovska on 3/11/18.
 */

public class User extends RealmObject {

    @SerializedName("id")
    private String id;
    @SerializedName("username")
    private String userName;
    @SerializedName("profile_image")
    private ProfileImage profileImage = new ProfileImage();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public ProfileImage getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(ProfileImage profileImage) {
        this.profileImage = profileImage;
    }
}
