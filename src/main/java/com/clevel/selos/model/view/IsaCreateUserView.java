package com.clevel.selos.model.view;

import com.clevel.selos.model.db.master.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class IsaCreateUserView implements Serializable {

    private String id;
    private String username;
    private String emailAddress;
    private String phoneExt;
    private String phoneNumber;
    private String buCode;
    private UserDepartment userDepartment;
    private Role role;
    private UserDivision userDivision;
    private UserRegion userRegion;
    private UserTeam userTeam;
    private UserTitle userTitle;
    private UserZone userZone;



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

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhoneExt() {
        return phoneExt;
    }

    public void setPhoneExt(String phoneExt) {
        this.phoneExt = phoneExt;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBuCode() {
        return buCode;
    }

    public void setBuCode(String buCode) {
        this.buCode = buCode;
    }

    public UserDepartment getUserDepartment() {
        return userDepartment;
    }

    public void setUserDepartment(UserDepartment userDepartment) {
        this.userDepartment = userDepartment;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public UserDivision getUserDivision() {
        return userDivision;
    }

    public void setUserDivision(UserDivision userDivision) {
        this.userDivision = userDivision;
    }

    public UserRegion getUserRegion() {
        return userRegion;
    }

    public void setUserRegion(UserRegion userRegion) {
        this.userRegion = userRegion;
    }

    public UserTeam getUserTeam() {
        return userTeam;
    }

    public void setUserTeam(UserTeam userTeam) {
        this.userTeam = userTeam;
    }

    public UserTitle getUserTitle() {
        return userTitle;
    }

    public void setUserTitle(UserTitle userTitle) {
        this.userTitle = userTitle;
    }

    public UserZone getUserZone() {
        return userZone;
    }

    public void setUserZone(UserZone userZone) {
        this.userZone = userZone;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("username", username)
                .append("emailAddress", emailAddress)
                .append("phoneExt", phoneExt)
                .append("phoneNumber", phoneNumber)
                .append("buCode", buCode)
                .append("userDepartment", userDepartment)
                .append("role", role)
                .append("userDivision", userDivision)
                .append("userRegion", userRegion)
                .append("userTeam", userTeam)
                .append("userTitle", userTitle)
                .append("userZone", userZone)
                .toString();
    }
}
