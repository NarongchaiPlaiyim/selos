package com.clevel.selos.model.report;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

public class ISAViewReport {

    private int row;
    private String userId;
    private String userName;
    private Date createDate;
    private Date login;
    private String status;
    private String numberOfDay;
    private String ipAddress;
    private String descrition;

    public ISAViewReport() {
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getLogin() {
        return login;
    }

    public void setLogin(Date login) {
        this.login = login;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNumberOfDay() {
        return numberOfDay;
    }

    public void setNumberOfDay(String numberOfDay) {
        this.numberOfDay = numberOfDay;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getDescrition() {
        return descrition;
    }

    public void setDescrition(String descrition) {
        this.descrition = descrition;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("row", row)
                .append("userId", userId)
                .append("userName", userName)
                .append("createDate", createDate)
                .append("login", login)
                .append("status", status)
                .append("numberOfDay", numberOfDay)
                .append("ipAddress", ipAddress)
                .append("descrition", descrition)
                .toString();
    }
}
