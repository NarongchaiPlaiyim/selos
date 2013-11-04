package com.clevel.selos.model.view;

import com.clevel.selos.model.UserStatus;
import com.clevel.selos.model.db.master.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class IsaSearchView implements Serializable {

    private String id;
    private String username;
    private Role roleId;
    private UserDepartment departmentId;
    private UserDivision divisionId;
    private UserRegion regionId;
    private UserTeam teamId;
    private UserTitle titleId;
    private UserZone zoneId;
    private UserStatus userStatus;

    public void reset() {
        this.id = "";
        this.username = "";
        this.roleId = new Role();
        this.departmentId = new UserDepartment();
        this.divisionId = new UserDivision();
        this.regionId = new UserRegion();
        this.teamId = new UserTeam();
        this.titleId = new UserTitle();
        this.zoneId = new UserZone();
//        this.userStatus = userStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Role getRoleId() {
        return roleId;
    }

    public void setRoleId(Role roleId) {
        this.roleId = roleId;
    }

    public UserDepartment getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(UserDepartment departmentId) {
        this.departmentId = departmentId;
    }

    public UserDivision getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(UserDivision divisionId) {
        this.divisionId = divisionId;
    }

    public UserRegion getRegionId() {
        return regionId;
    }

    public void setRegionId(UserRegion regionId) {
        this.regionId = regionId;
    }

    public UserTeam getTeamId() {
        return teamId;
    }

    public void setTeamId(UserTeam teamId) {
        this.teamId = teamId;
    }

    public UserTitle getTitleId() {
        return titleId;
    }

    public void setTitleId(UserTitle titleId) {
        this.titleId = titleId;
    }

    public UserZone getZoneId() {
        return zoneId;
    }

    public void setZoneId(UserZone zoneId) {
        this.zoneId = zoneId;
    }

    public UserStatus getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("username", username)
                .append("roleId", roleId)
                .append("departmentId", departmentId)
                .append("divisionId", divisionId)
                .append("regionId", regionId)
                .append("teamId", teamId)
                .append("titleId", titleId)
                .append("zoneId", zoneId)
                .append("userStatus", userStatus)
                .toString();
    }
}
