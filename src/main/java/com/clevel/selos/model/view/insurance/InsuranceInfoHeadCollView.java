package com.clevel.selos.model.view.insurance;

import java.io.Serializable;
import java.math.BigDecimal;

public class InsuranceInfoHeadCollView implements Serializable {
    private String titleDeed;
    private BigDecimal exitingCredit;

    public InsuranceInfoHeadCollView() {

    }

    public String getTitleDeed() {
        return titleDeed;
    }

    public void setTitleDeed(String titleDeed) {
        this.titleDeed = titleDeed;
    }

    public BigDecimal getExitingCredit() {
        return exitingCredit;
    }

    public void setExitingCredit(BigDecimal exitingCredit) {
        this.exitingCredit = exitingCredit;
    }
}
