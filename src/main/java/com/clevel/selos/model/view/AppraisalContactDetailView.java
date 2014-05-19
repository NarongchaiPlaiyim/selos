package com.clevel.selos.model.view;

import com.clevel.selos.model.db.master.User;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Date;

public class AppraisalContactDetailView implements Serializable {
    private long id;
    private long no1;
    private long contractId1;
    private String customerName1;
    private String contactNo1;
    private long no2;
    private long contractId2;
    private String customerName2;
    private String contactNo2;
    private long no3;
    private long contractId3;
    private String customerName3;
    private String contactNo3;
    private Date createDate;
    private Date modifyDate;
    private User createBy;
    private User modifyBy;

    public AppraisalContactDetailView() {
        init();
    }

    private void init(){

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getNo1() {
        return no1;
    }

    public void setNo1(int no1) {
        this.no1 = no1;
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

    public long getNo2() {
        return no2;
    }

    public void setNo2(int no2) {
        this.no2 = no2;
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

    public long getNo3() {
        return no3;
    }

    public void setNo1(long no1) {
        this.no1 = no1;
    }

    public void setNo2(long no2) {
        this.no2 = no2;
    }

    public void setNo3(long no3) {
        this.no3 = no3;
    }

    public void setNo3(int no3) {
        this.no3 = no3;
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

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public User getCreateBy() {
        return createBy;
    }

    public void setCreateBy(User createBy) {
        this.createBy = createBy;
    }

    public User getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(User modifyBy) {
        this.modifyBy = modifyBy;
    }

    public long getContractId1() {
        return contractId1;
    }

    public void setContractId1(long contractId1) {
        this.contractId1 = contractId1;
    }

    public long getContractId2() {
        return contractId2;
    }

    public void setContractId2(long contractId2) {
        this.contractId2 = contractId2;
    }

    public long getContractId3() {
        return contractId3;
    }

    public void setContractId3(long contractId3) {
        this.contractId3 = contractId3;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("no1", no1)
                .append("contractId1", contractId1)
                .append("customerName1", customerName1)
                .append("contactNo1", contactNo1)
                .append("no2", no2)
                .append("contractId2", contractId2)
                .append("customerName2", customerName2)
                .append("contactNo2", contactNo2)
                .append("no3", no3)
                .append("contractId3", contractId3)
                .append("customerName3", customerName3)
                .append("contactNo3", contactNo3)
                .append("createDate", createDate)
                .append("modifyDate", modifyDate)
                .append("createBy", createBy)
                .append("modifyBy", modifyBy)
                .toString();
    }
}
