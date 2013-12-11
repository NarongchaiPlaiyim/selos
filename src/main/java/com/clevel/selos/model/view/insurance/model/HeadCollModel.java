package com.clevel.selos.model.view.insurance.model;

import java.io.Serializable;

public class HeadCollModel implements Serializable {
    private String titleDeed;
    private int exitingCredit;

    public HeadCollModel() {

    }

    public String getTitleDeed() {
        return titleDeed;
    }

    public void setTitleDeed(String titleDeed) {
        this.titleDeed = titleDeed;
    }

    public int getExitingCredit() {
        return exitingCredit;
    }

    public void setExitingCredit(int exitingCredit) {
        this.exitingCredit = exitingCredit;
    }
}
