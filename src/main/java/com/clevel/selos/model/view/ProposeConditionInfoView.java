package com.clevel.selos.model.view;

import java.io.Serializable;

public class ProposeConditionInfoView implements Serializable {
    private long id;
    private String loanType;
    private String conditionDesc;

    public ProposeConditionInfoView() {
        reset();
    }

    public void reset() {
        this.loanType = "";
        this.conditionDesc = "";
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public String getConditionDesc() {
        return conditionDesc;
    }

    public void setConditionDesc(String conditionDesc) {
        this.conditionDesc = conditionDesc;
    }
}
