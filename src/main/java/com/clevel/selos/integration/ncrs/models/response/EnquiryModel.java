package com.clevel.selos.integration.ncrs.models.response;
public class EnquiryModel {
    
    private String enqdate;
    private String enqtime;
    private String membercode;
    private String shortname;
    private String enqpurpose;
    private String enqamount;
    private String currencycode;

    public EnquiryModel(String enqdate, String enqtime, String membercode, String shortname, String enqpurpose, String enqamount, String currencycode) {
        this.enqdate = enqdate;
        this.enqtime = enqtime;
        this.membercode = membercode;
        this.shortname = shortname;
        this.enqpurpose = enqpurpose;
        this.enqamount = enqamount;
        this.currencycode = currencycode;
    }

    public String getEnqdate() {
        return enqdate;
    }

    public String getEnqtime() {
        return enqtime;
    }

    public String getMembercode() {
        return membercode;
    }

    public String getShortname() {
        return shortname;
    }

    public String getEnqpurpose() {
        return enqpurpose;
    }

    public String getEnqamount() {
        return enqamount;
    }

    public String getCurrencycode() {
        return currencycode;
    }
    
    
}
