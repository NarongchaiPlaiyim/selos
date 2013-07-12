package com.clevel.selos.model.db;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "MST_USER")
public class User {
    @Id
    @Column(name = "USER_ID")
    long id;
    @Column(name = "USER_NAME")
    String userName;
    @Column(name = "USER_PASSWORD")
    String password;
    @OneToMany(mappedBy="user",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    List<Role> roles;

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

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("id", id).
                append("userName", userName).
                append("password", password).
                append("roles", roles).
                toString();
    }
}
