package com.clevel.selos.integration.corebanking.model;

import java.io.Serializable;

public class CardTypeView implements Serializable {

    public CardTypeView(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    private String name;
    private String value;
}
