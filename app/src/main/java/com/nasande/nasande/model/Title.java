package com.nasande.nasande.model;

import com.google.gson.annotations.SerializedName;

public class Title {

    private String value;
    private String lang;

    public Title(String value) {
        this.value = value;
    }

    @SerializedName("value")
    public String getValue() {
        return this.value;
    }
}