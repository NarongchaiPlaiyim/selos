package com.clevel.selos.model.report;


import com.clevel.selos.report.ReportModel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class AppraisalContactDetailViewReport extends ReportModel {

    private int count1;
    private String customerName1;
    private String contactNo1;
    private int count2;
    private String customerName2;
    private String contactNo2;
    private int count3;
    private String customerName3;
    private String contactNo3;

    public AppraisalContactDetailViewReport() {
    }

    public String getCustomerName1() {
        return customerName1;
    }

    public void setCustomerName1(String customerName1) {
        this.customerName1 = customerName1;
    }

    public String getContactNo1() {
        return contactNo1;
    }

    public void setContactNo1(String contactNo1) {
        this.contactNo1 = contactNo1;
    }

    public String getCustomerName2() {
        return customerName2;
    }

    public void setCustomerName2(String customerName2) {
        this.customerName2 = customerName2;
    }

    public String getContactNo2() {
        return contactNo2;
    }

    public void setContactNo2(String contactNo2) {
        this.contactNo2 = contactNo2;
    }

    public String getCustomerName3() {
        return customerName3;
    }

    public void setCustomerName3(String customerName3) {
        this.customerName3 = customerName3;
    }

    public String getContactNo3() {
        return contactNo3;
    }

    public void setContactNo3(String contactNo3) {
        this.contactNo3 = contactNo3;
    }


    public int getCount1() {
        return count1;
    }

    public void setCount1(int count1) {
        this.count1 = count1;
    }

    public int getCount2() {
        return count2;
    }

    public void setCount2(int count2) {
        this.count2 = count2;
    }

    public int getCount3() {
        return count3;
    }

    public void setCount3(int count3) {
        this.count3 = count3;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("customerName1", customerName1)
                .append("contactNo1", contactNo1)
                .append("customerName2", customerName2)
                .append("contactNo2", contactNo2)
                .append("customerName3", customerName3)
                .append("contactNo3", contactNo3)
                .toString();
    }
}
