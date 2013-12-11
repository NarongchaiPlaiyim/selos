package com.clevel.selos.model.view;

import java.io.Serializable;
import java.util.Date;

public class MortgageSummaryView implements Serializable {
    private long id;
    private int approvedType;
    private Date loanDate;
    private int signContract;
//    signDetail // drop down
    private int comsNo;



    public MortgageSummaryView() {
        reset();
    }

    public void reset() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getApprovedType() {
        return approvedType;
    }

    public void setApprovedType(int approvedType) {
        this.approvedType = approvedType;
    }

    public Date getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(Date loanDate) {
        this.loanDate = loanDate;
    }

    public int getSignContract() {
        return signContract;
    }

    public void setSignContract(int signContract) {
        this.signContract = signContract;
    }

    public int getComsNo() {
        return comsNo;
    }

    public void setComsNo(int comsNo) {
        this.comsNo = comsNo;
    }
}
