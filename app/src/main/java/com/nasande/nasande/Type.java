package com.nasande.nasande;

import com.google.gson.annotations.SerializedName;

public class Type {

    private String href;

    public Type(String href) {
        this.href = href;
    }

    @SerializedName("href")
    public String getTargetId() {
        return this.href;
    }
}
