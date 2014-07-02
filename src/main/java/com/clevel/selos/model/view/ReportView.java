package com.clevel.selos.model.view;


import java.io.Serializable;

public class ReportView implements Serializable {

    private String nameReportOpShect;
    private String nameReportExSum;
    private String nameReportRejectLetter;
    private String nameReportAppralsal;
    private String nameReportOfferLetter;
    private String nameISAReportViolation;
    private String nameISAReportUserProfile;
    private String nameISAReportLogonOver90;


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

    public String getNameReportOfferLetter() {
        return nameReportOfferLetter;
    }

    public void setNameReportOfferLetter(String nameReportOfferLetter) {
        this.nameReportOfferLetter = nameReportOfferLetter;
    }

    public String getNameISAReportViolation() {
        return nameISAReportViolation;
    }

    public void setNameISAReportViolation(String nameISAReportViolation) {
        this.nameISAReportViolation = nameISAReportViolation;
    }

    public String getNameISAReportUserProfile() {
        return nameISAReportUserProfile;
    }

    public void setNameISAReportUserProfile(String nameISAReportUserProfile) {
        this.nameISAReportUserProfile = nameISAReportUserProfile;
    }

    public String getNameISAReportLogonOver90() {
        return nameISAReportLogonOver90;
    }

    public void setNameISAReportLogonOver90(String nameISAReportLogonOver90) {
        this.nameISAReportLogonOver90 = nameISAReportLogonOver90;
    }
}
