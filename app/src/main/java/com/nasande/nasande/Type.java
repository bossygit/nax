package com.nasande.nasande;

import com.google.gson.annotations.SerializedName;

public class Type {

    private String target_id;

    public Type(String target_id) {
        this.target_id = target_id;
    }

    @SerializedName("target_id")
    public String getTargetId() {
        return this.target_id;
    }
}
