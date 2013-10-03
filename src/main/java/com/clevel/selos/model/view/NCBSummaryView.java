package com.clevel.selos.model.view;

import java.io.Serializable;

public class NCBSummaryView implements Serializable {

    private String cusName;
    private String cusType;
    private String personalID;
    private String noOfNCBCheckIn6months;
    private String typeOfCurrentPayment;
    private String typeOfHistoryPayment;
    private String nplFlag;
    private String tdrStatusFromNCB;
    private String tdrPaymentCondition;


    public NCBSummaryView(){
    }

    public String getCusName() {
        return cusName;
    }

    public void setCusName(String cusName) {
        this.cusName = cusName;
    }

    public String getCusType() {
        return cusType;
    }

    public void setCusType(String cusType) {
        this.cusType = cusType;
    }

    public String getPersonalID() {
        return personalID;
    }

    public void setPersonalID(String personalID) {
        this.personalID = personalID;
    }

    public String getNoOfNCBCheckIn6months() {
        return noOfNCBCheckIn6months;
    }

    public void setNoOfNCBCheckIn6months(String noOfNCBCheckIn6months) {
        this.noOfNCBCheckIn6months = noOfNCBCheckIn6months;
    }

    public String getTypeOfCurrentPayment() {
        return typeOfCurrentPayment;
    }

    public void setTypeOfCurrentPayment(String typeOfCurrentPayment) {
        this.typeOfCurrentPayment = typeOfCurrentPayment;
    }

    public String getTypeOfHistoryPayment() {
        return typeOfHistoryPayment;
    }

    public void setTypeOfHistoryPayment(String typeOfHistoryPayment) {
        this.typeOfHistoryPayment = typeOfHistoryPayment;
    }

    public String getNplFlag() {
        return nplFlag;
    }

    public void setNplFlag(String nplFlag) {
        this.nplFlag = nplFlag;
    }

    public String getTdrStatusFromNCB() {
        return tdrStatusFromNCB;
    }

    public void setTdrStatusFromNCB(String tdrStatusFromNCB) {
        this.tdrStatusFromNCB = tdrStatusFromNCB;
    }

    public String getTdrPaymentCondition() {
        return tdrPaymentCondition;
    }

    public void setTdrPaymentCondition(String tdrPaymentCondition) {
        this.tdrPaymentCondition = tdrPaymentCondition;
    }
}
