package com.ziomacki.stackoverflowclient.search.model;

import com.google.gson.annotations.SerializedName;

public class Owner {
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
}
