package com.clevel.selos.model;

import java.io.Serializable;

public class ManageButton implements Serializable {
    private boolean checkNCBButton;

    public boolean isCheckNCBButton() {
        return checkNCBButton;
    }

    public void setCheckNCBButton(boolean checkNCBButton) {
        this.checkNCBButton = checkNCBButton;
    }
}
