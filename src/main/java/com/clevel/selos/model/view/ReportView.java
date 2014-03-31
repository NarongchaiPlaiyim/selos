package com.clevel.selos.model.view;


import java.io.Serializable;

public class ReportView implements Serializable {

    private String nameReportOpShect;
    private String nameReportExSum;
    private String nameReportRejectLetter;
    private String nameReportAppralsal;


    public ReportView() {
    }

    public String getNameReportExSum() {
        return nameReportExSum;
    }

    public void setNameReportExSum(String nameReportExSum) {
        this.nameReportExSum = nameReportExSum;
    }

    public String getNameReportOpShect() {
        return nameReportOpShect;
    }

    public void setNameReportOpShect(String nameReportOpShect) {
        this.nameReportOpShect = nameReportOpShect;
    }

    public String getNameReportRejectLetter() {
        return nameReportRejectLetter;
    }

    public void setNameReportRejectLetter(String nameReportRejectLetter) {
        this.nameReportRejectLetter = nameReportRejectLetter;
    }

    public String getNameReportAppralsal() {
        return nameReportAppralsal;
    }

    public void setNameReportAppralsal(String nameReportAppralsal) {
        this.nameReportAppralsal = nameReportAppralsal;
    }
}
