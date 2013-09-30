package com.clevel.selos.model.db.relation;

import com.clevel.selos.model.db.master.AuthorizationDOA;
import com.clevel.selos.model.db.master.User;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "rel_user_authorization_doa")
public class UserToAuthorizationDOA implements Serializable {
    @Id
    @Column(name = "id")
    private int id;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    @OneToOne
    @JoinColumn(name = "authorizationdoa_id")
    private AuthorizationDOA authorizationDOA;
    @Column(name = "alternate_flag")
    private int alternateFlag;
    @Column(name = "active")
    private int active;

    public UserToAuthorizationDOA() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public AuthorizationDOA getAuthorizationDOA() {
        return authorizationDOA;
    }

    public void setAuthorizationDOA(AuthorizationDOA authorizationDOA) {
        this.authorizationDOA = authorizationDOA;
    }

    public int getAlternateFlag() {
        return alternateFlag;
    }

    public void setAlternateFlag(int alternateFlag) {
        this.alternateFlag = alternateFlag;
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
                append("user", user).
                append("authorizationDOA", authorizationDOA).
                append("alternateFlag", alternateFlag).
                append("active", active).
                toString();
    }
}
