package com.clevel.selos.model.view.isa;

import com.clevel.selos.businesscontrol.isa.ValidationImp;
import com.clevel.selos.model.CommandType;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class CSVModel extends ValidationImp implements Serializable {
    private String commandType;
    private String userId;
    private String userName;
    private String active;
    private String role;
    private String department;
    private String division;
    private String region;
    private String team;
    private String title;
//    private String status;


    private String Seq;
    private String EmployeeID;
    private String EmployeeName;
    private String TeamID;
    private String TeamName;
    private String CreateDate;
    private String LastSignOnDate;
    private String Status;
    private String NumberOfDays;

    public CSVModel() {

    }

    public String getCommandType() {
        return commandType;
    }

    public void setCommandType(String commandType) {
        this.commandType = commandType;
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

//    public String getStatus() {
//        return status;
//    }
//
//    public void setStatus(String status) {
//        this.status = status;
//    }

    public String valid(CommandType commandType) {
        return super.valid(this, commandType);
    }

    public String getSeq() {
        return Seq;
    }

    public void setSeq(String seq) {
        Seq = seq;
    }

    public String getEmployeeID() {
        return EmployeeID;
    }

    public void setEmployeeID(String employeeID) {
        EmployeeID = employeeID;
    }

    public String getEmployeeName() {
        return EmployeeName;
    }

    public void setEmployeeName(String employeeName) {
        EmployeeName = employeeName;
    }

    public String getTeamID() {
        return TeamID;
    }

    public void setTeamID(String teamID) {
        TeamID = teamID;
    }

    public String getTeamName() {
        return TeamName;
    }

    public void setTeamName(String teamName) {
        TeamName = teamName;
    }

    public String getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(String createDate) {
        CreateDate = createDate;
    }

    public String getLastSignOnDate() {
        return LastSignOnDate;
    }

    public void setLastSignOnDate(String lastSignOnDate) {
        LastSignOnDate = lastSignOnDate;
    }

    public String getNumberOfDays() {
        return NumberOfDays;
    }

    public void setNumberOfDays(String numberOfDays) {
        NumberOfDays = numberOfDays;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("commandType", commandType)
                .append("userId", userId)
                .append("userName", userName)
                .append("active", active)
                .append("role", role)
                .append("department", department)
                .append("division", division)
                .append("region", region)
                .append("team", team)
                .append("title", title)
                .append("Seq", Seq)
                .append("EmployeeID", EmployeeID)
                .append("EmployeeName", EmployeeName)
                .append("TeamID", TeamID)
                .append("TeamName", TeamName)
                .append("CreateDate", CreateDate)
                .append("LastSignOnDate", LastSignOnDate)
                .append("Status", Status)
                .append("NumberOfDays", NumberOfDays)
                .toString();
    }

    public String toStringForAudit() {
        return new StringBuilder()
                .append("From CSV --> ")
                .append("commandType[").append(commandType).append("]")
                .append("userId[").append(userId).append("]")
                .append("userName[").append(userName).append("]")
                .append("active[").append(active).append("]")
                .append("role[").append(role).append("]")
                .append("department[").append(department).append("]")
                .append("division[").append(division).append("]")
                .append("region[").append(region).append("]")
                .append("team[").append(team).append("]")
                .append("title[").append(title).append("]")
                .append("Status[").append(Status).append("]")
                .toString();
    }
}
