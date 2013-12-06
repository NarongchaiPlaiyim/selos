package com.clevel.selos.model.view.openaccount.model;

import java.io.Serializable;

public class PurposeModel implements Serializable {
    private long id;
    private boolean isSelected;
    private String name;

    public PurposeModel() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
