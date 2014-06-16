package com.clevel.selos.model.report;

public class NCBRecordExsumReport {
    private String ncbCusName;
    private int checkIn6Month;
    private String currentPaymentType;
    private String historyPaymentType;
    private String nplFlagText;
    private String tdrFlagText;
    private String description;
    private String paymentClass;

    public NCBRecordExsumReport() {
        ncbCusName = "";
        checkIn6Month = 0;
        currentPaymentType = "";
        historyPaymentType = "";
        nplFlagText = "";
        tdrFlagText = "";
        description = "";
        paymentClass = "";
    }

    public String getNcbCusName() {
        return ncbCusName;
    }

    public void setNcbCusName(String ncbCusName) {
        this.ncbCusName = ncbCusName;
    }

    public int getCheckIn6Month() {
        return checkIn6Month;
    }

    public void setCheckIn6Month(int checkIn6Month) {
        this.checkIn6Month = checkIn6Month;
    }

    public String getCurrentPaymentType() {
        return currentPaymentType;
    }

    public void setCurrentPaymentType(String currentPaymentType) {
        this.currentPaymentType = currentPaymentType;
    }

    public String getHistoryPaymentType() {
        return historyPaymentType;
    }

    public void setHistoryPaymentType(String historyPaymentType) {
        this.historyPaymentType = historyPaymentType;
    }

    public String getNplFlagText() {
        return nplFlagText;
    }

    public void setNplFlagText(String nplFlagText) {
        this.nplFlagText = nplFlagText;
    }

    public String getTdrFlagText() {
        return tdrFlagText;
    }

    public void setTdrFlagText(String tdrFlagText) {
        this.tdrFlagText = tdrFlagText;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPaymentClass() {
        return paymentClass;
    }

    public void setPaymentClass(String paymentClass) {
        this.paymentClass = paymentClass;
    }

    @Override
    public String toString() {
        return "NCBRecordExsumReport{" +
                "ncbCusName='" + ncbCusName + '\'' +
                ", checkIn6Month=" + checkIn6Month +
                ", currentPaymentType='" + currentPaymentType + '\'' +
                ", historyPaymentType='" + historyPaymentType + '\'' +
                ", nplFlagText='" + nplFlagText + '\'' +
                ", tdrFlagText='" + tdrFlagText + '\'' +
                ", description='" + description + '\'' +
                ", paymentClass='" + paymentClass + '\'' +
                '}';
    }
}
