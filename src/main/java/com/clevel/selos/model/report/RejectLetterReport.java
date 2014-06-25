package com.clevel.selos.model.report;

import com.clevel.selos.report.ReportModel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class RejectLetterReport extends ReportModel{

    private String appNumber;
    private String name;
    private String address;
    private String date;

    private int typeNCB;
    private int typePolicy;
    private int typeIncome;

    public RejectLetterReport() {
        typeNCB = getDefaultInteger();
        typePolicy = getDefaultInteger();
        typeIncome = getDefaultInteger();
    }

    public String getAppNumber() {
        return appNumber;
    }

    public void setAppNumber(String appNumber) {
        this.appNumber = appNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getTypeNCB() {
        return typeNCB;
    }

    public void setTypeNCB(int typeNCB) {
        this.typeNCB = typeNCB;
    }

    public int getTypePolicy() {
        return typePolicy;
    }

    public void setTypePolicy(int typePolicy) {
        this.typePolicy = typePolicy;
    }

    public int getTypeIncome() {
        return typeIncome;
    }

    public void setTypeIncome(int typeIncome) {
        this.typeIncome = typeIncome;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("appNumber", appNumber)
                .append("name", name)
                .append("address", address)
                .append("date", date)
                .append("typeNCB", typeNCB)
                .append("typePolicy", typePolicy)
                .append("typeIncome", typeIncome)
                .toString();
    }
}
