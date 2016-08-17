package com.ziomacki.stackoverflowclient.search.model;

import com.google.gson.annotations.SerializedName;
import io.realm.RealmObject;

public class Owner extends RealmObject {
    @SerializedName("display_name")
    private String displayName;
    @SerializedName("profile_image")
    private String profileImage;

    public String getDisplayName() {
        return displayName;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
