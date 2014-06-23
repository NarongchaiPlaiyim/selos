package com.clevel.selos.businesscontrol.isa.csv.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class CSVModel implements Serializable {
    private String userId;
    private String userName;
    private String active;
    private  String role;
    private  String department;
    private  String division;
    private  String region;
    private  String team;
    private  String title;
    private  String status;

    public CSVModel() {

    }

    public CSVModel(String userId, String userName, String active, String role, String department, String division, String region, String team, String title, String status) {
        this.userId = userId;
        this.userName = userName;
        this.active = active;
        this.role = role;
        this.department = department;
        this.division = division;
        this.region = region;
        this.team = team;
        this.title = title;
        this.status = status;
    }

    public CSVModel createRecord(String userId, String userName, String active, String role, String department, String division, String region, String team, String title, String status) {
        return new CSVModel(userId, userName, active, role, department, division, region, team, title, status);
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

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("userId", userId)
                .append("userName", userName)
                .append("active", active)
                .append("role", role)
                .append("department", department)
                .append("division", division)
                .append("region", region)
                .append("team", team)
                .append("title", title)
                .append("status", status)
                .toString();
    }
}
