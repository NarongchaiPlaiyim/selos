package com.clevel.selos.model.view;

import java.io.Serializable;

public class UWRuleNameView implements Serializable{

    private int id;
    private String description;
    private String brmsCode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBrmsCode() {
        return brmsCode;
    }

    public void setBrmsCode(String brmsCode) {
        this.brmsCode = brmsCode;
    }
}
