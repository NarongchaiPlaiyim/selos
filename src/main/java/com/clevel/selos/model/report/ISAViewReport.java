package com.clevel.selos.model.report;

import com.clevel.selos.model.db.master.Role;
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
    private String buCode;
    private String team;
    private String testId;
    private Date modifyDate;
    private String createBy;
    private String modifyBy;
    private String adminTask;
    private String empID;
    private String empName;
    private String oldData;
    private String newData;
    private String adminName;
    private Date lastLogOn;
    private String active;
    private String department;
    private String division;
    private String region;
    private String title;

    //Matrix
    private String screenId;
    private String screenName;
    private String roleId;
    private String roleName;

    private int role;

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

    public String getBuCode() {
        return buCode;
    }

    public void setBuCode(String buCode) {
        this.buCode = buCode;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(String modifyBy) {
        this.modifyBy = modifyBy;
    }

    public String getAdminTask() {
        return adminTask;
    }

    public void setAdminTask(String adminTask) {
        this.adminTask = adminTask;
    }

    public String getEmpID() {
        return empID;
    }

    public void setEmpID(String empID) {
        this.empID = empID;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getOldData() {
        return oldData;
    }

    public void setOldData(String oldData) {
        this.oldData = oldData;
    }

    public String getNewData() {
        return newData;
    }

    public void setNewData(String newData) {
        this.newData = newData;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }

    public Date getLastLogOn() {
        return lastLogOn;
    }

    public void setLastLogOn(Date lastLogOn) {
        this.lastLogOn = lastLogOn;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getScreenId() {
        return screenId;
    }

    public void setScreenId(String screenId) {
        this.screenId = screenId;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
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
                .append("buCode", buCode)
                .append("team", team)
                .append("testId", testId)
                .append("modifyDate", modifyDate)
                .append("createBy", createBy)
                .append("modifyBy", modifyBy)
                .append("adminTask", adminTask)
                .append("empID", empID)
                .append("empName", empName)
                .append("oldData", oldData)
                .append("newData", newData)
                .append("adminName", adminName)
                .append("lastLogOn", lastLogOn)
                .append("active", active)
                .append("department", department)
                .append("division", division)
                .append("region", region)
                .append("title", title)
                .append("screenId", screenId)
                .append("screenName", screenName)
                .append("roleId", roleId)
                .append("roleName", roleName)
                .append("role", role)
                .toString();
    }
}
