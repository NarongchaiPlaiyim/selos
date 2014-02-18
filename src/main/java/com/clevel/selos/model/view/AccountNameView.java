package com.clevel.selos.model.view;

import java.io.Serializable;

public class AccountNameView implements Serializable {
    private String name;
    private long id;

    public AccountNameView(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
