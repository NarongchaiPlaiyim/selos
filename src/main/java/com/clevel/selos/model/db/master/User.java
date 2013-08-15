package com.clevel.selos.model.db.master;

import com.clevel.selos.model.db.master.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "mst_user")
public class User {
    @Id
    @Column(name = "id")
    private long id;
    @Column(name = "username")
    private String userName;
    @Column(name = "password")
    private String password;
    @OneToOne
    @JoinColumn(name="role_id")
    private Role role;
    @Column(name = "bu_code")
    private String buCode;
    @Column(name = "email_address")
    private String emailAddress;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="last_logon")
    private Date lastLogon;
    @Column(name="last_pc")
    private String lastPC;
    @OneToOne
    @JoinColumn(name="team_id")
    private UserTeam team;
    @OneToOne
    @JoinColumn(name="zone_id")
    private UserZone zone;
    @OneToOne
    @JoinColumn(name="region_id")
    private UserRegion region;
    @OneToOne
    @JoinColumn(name="title_id")
    private UserTitle title;
    @Column(name="phone_number")
    private String phoneNumber;
    @Column(name="phone_ext")
    private String phoneExt;
    @OneToOne
    @JoinColumn(name="deportment_id")
    private UserDepartment department;
    @OneToOne
    @JoinColumn(name="division_id")
    private UserDivision division;
    @Column(name="administrator")
    private int administrator;
    @Column(name="active")
    private int active;

    public User() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getBuCode() {
        return buCode;
    }

    public void setBuCode(String buCode) {
        this.buCode = buCode;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public Date getLastLogon() {
        return lastLogon;
    }

    public void setLastLogon(Date lastLogon) {
        this.lastLogon = lastLogon;
    }

    public String getLastPC() {
        return lastPC;
    }

    public void setLastPC(String lastPC) {
        this.lastPC = lastPC;
    }

    public UserTeam getTeam() {
        return team;
    }

    public void setTeam(UserTeam team) {
        this.team = team;
    }

    public UserZone getZone() {
        return zone;
    }

    public void setZone(UserZone zone) {
        this.zone = zone;
    }

    public UserRegion getRegion() {
        return region;
    }

    public void setRegion(UserRegion region) {
        this.region = region;
    }

    public UserTitle getTitle() {
        return title;
    }

    public void setTitle(UserTitle title) {
        this.title = title;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneExt() {
        return phoneExt;
    }

    public void setPhoneExt(String phoneExt) {
        this.phoneExt = phoneExt;
    }

    public UserDepartment getDepartment() {
        return department;
    }

    public void setDepartment(UserDepartment department) {
        this.department = department;
    }

    public UserDivision getDivision() {
        return division;
    }

    public void setDivision(UserDivision division) {
        this.division = division;
    }

    public int getAdministrator() {
        return administrator;
    }

    public void setAdministrator(int administrator) {
        this.administrator = administrator;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("id", id).
                append("userName", userName).
                append("password", password).
                append("role", role).
                append("buCode", buCode).
                append("emailAddress", emailAddress).
                append("lastLogon", lastLogon).
                append("lastPC", lastPC).
                append("team", team).
                append("zone", zone).
                append("region", region).
                append("title", title).
                append("phoneNumber", phoneNumber).
                append("phoneExt", phoneExt).
                append("department", department).
                append("division", division).
                append("administrator", administrator).
                append("active", active).
                toString();
    }
}