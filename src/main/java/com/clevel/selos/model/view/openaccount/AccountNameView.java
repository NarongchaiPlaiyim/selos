package com.clevel.selos.model.view.openaccount;

import java.io.Serializable;

public class AccountNameView implements Serializable {
    private String name;

    public AccountNameView() {
    }

    public AccountNameView(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
