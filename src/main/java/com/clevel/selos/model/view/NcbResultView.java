package com.clevel.selos.model.view;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: acer
 * Date: 18/9/2556
 * Time: 14:06 น.
 * To change this template use File | Settings | File Templates.
 */

public class NcbResultView {

    private Date ncbCheckingDate;
    private int  noOfNCBCheckIn6months;
    private String paymentClass;
    private String typeOfCurrentPayment;
    private String typeOfHistoryPayment;
    private String nplFlag;
    private String tdrFlag;
    private String tdrPaymentCondition;
    private String remark ;

    public void NcbResultView(){

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

    public String getPaymentClass() {
        return paymentClass;
    }

    public void setPaymentClass(String paymentClass) {
        this.paymentClass = paymentClass;
    }

    public int getNoOfNCBCheckIn6months() {
        return noOfNCBCheckIn6months;
    }

    public void setNoOfNCBCheckIn6months(int noOfNCBCheckIn6months) {
        this.noOfNCBCheckIn6months = noOfNCBCheckIn6months;
    }

    public Date getNcbCheckingDate() {
        return ncbCheckingDate;
    }

    public void setNcbCheckingDate(Date ncbCheckingDate) {
        this.ncbCheckingDate = ncbCheckingDate;
    }

    public String getNplFlag() {
        return nplFlag;
    }

    public void setNplFlag(String nplFlag) {
        this.nplFlag = nplFlag;
    }

    public String getTdrFlag() {
        return tdrFlag;
    }

    public void setTdrFlag(String tdrFlag) {
        this.tdrFlag = tdrFlag;
    }

    public String getTdrPaymentCondition() {
        return tdrPaymentCondition;
    }

    public void setTdrPaymentCondition(String tdrPaymentCondition) {
        this.tdrPaymentCondition = tdrPaymentCondition;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
