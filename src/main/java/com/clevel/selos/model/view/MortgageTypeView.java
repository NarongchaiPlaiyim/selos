package com.clevel.selos.model.view;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class MortgageTypeView implements Serializable {
    private int id;
    private int active;
    private String mortgage;
    private boolean redeem;
    private boolean mortgageFeeFlag;
    private boolean mortgageFlag;
    private boolean pledgeFlag;
    private boolean guarantorFlag;
    private boolean tcgFlag;
    private boolean referredFlag;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public String getMortgage() {
        return mortgage;
    }

    public void setMortgage(String mortgage) {
        this.mortgage = mortgage;
    }

    public boolean isRedeem() {
        return redeem;
    }

    public void setRedeem(boolean redeem) {
        this.redeem = redeem;
    }

    public boolean isMortgageFeeFlag() {
        return mortgageFeeFlag;
    }

    public void setMortgageFeeFlag(boolean mortgageFeeFlag) {
        this.mortgageFeeFlag = mortgageFeeFlag;
    }

    public boolean isMortgageFlag() {
        return mortgageFlag;
    }

    public void setMortgageFlag(boolean mortgageFlag) {
        this.mortgageFlag = mortgageFlag;
    }

    public boolean isPledgeFlag() {
        return pledgeFlag;
    }

    public void setPledgeFlag(boolean pledgeFlag) {
        this.pledgeFlag = pledgeFlag;
    }

    public boolean isGuarantorFlag() {
        return guarantorFlag;
    }

    public void setGuarantorFlag(boolean guarantorFlag) {
        this.guarantorFlag = guarantorFlag;
    }

    public boolean isTcgFlag() {
        return tcgFlag;
    }

    public void setTcgFlag(boolean tcgFlag) {
        this.tcgFlag = tcgFlag;
    }

    public boolean isReferredFlag() {
        return referredFlag;
    }

    public void setReferredFlag(boolean referredFlag) {
        this.referredFlag = referredFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("active", active)
                .append("mortgage", mortgage)
                .append("redeem", redeem)
                .append("mortgageFeeFlag", mortgageFeeFlag)
                .append("mortgageFlag", mortgageFlag)
                .append("pledgeFlag", pledgeFlag)
                .append("guarantorFlag", guarantorFlag)
                .append("tcgFlag", tcgFlag)
                .append("referredFlag", referredFlag)
                .toString();
    }
}
