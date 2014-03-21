package com.clevel.selos.model.view;


import java.io.Serializable;

public class ReportView implements Serializable {

    private String nameReportOpShect;
    private String nameReportExSum;

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
}
