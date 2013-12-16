package com.clevel.selos.model.view.insurance;

import java.io.Serializable;

public class InsuranceInfoHeadCollView implements Serializable {
    private String titleDeed;
    private int exitingCredit;

    public InsuranceInfoHeadCollView() {

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
