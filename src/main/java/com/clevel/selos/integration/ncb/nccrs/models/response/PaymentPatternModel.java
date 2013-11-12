package com.clevel.selos.integration.ncb.nccrs.models.response;

import java.io.Serializable;

public class PaymentPatternModel implements Serializable {
    private String pattern;
    private String startmonth;
    private String endmonth;
    private String over30days;
    private String over60days;
    private String over90days;

    public String getPattern() {
        return pattern;
    }

    public String getStartmonth() {
        return startmonth;
    }

    public String getEndmonth() {
        return endmonth;
    }

    public String getOver30days() {
        return over30days;
    }

    public String getOver60days() {
        return over60days;
    }

    public String getOver90days() {
        return over90days;
    }
}
