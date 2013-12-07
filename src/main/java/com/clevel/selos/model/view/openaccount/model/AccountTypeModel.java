package com.clevel.selos.model.view.openaccount.model;

import java.io.Serializable;

public class AccountTypeModel implements Serializable {
    private int id;
    private String name;

    public AccountTypeModel() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
