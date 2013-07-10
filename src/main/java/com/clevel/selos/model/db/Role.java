package com.clevel.selos.model.db;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;

@Entity
@Table(name = "SLOS_MS_ROLE")
public class Role {
    @Id
    @Column(name = "ROLE_ID")
    long id;
    @Column(name = "ROLE_NAME")
    String name;
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    User user;

    public Role() {
    }

    public Role(String name, User user) {
        this.name = name;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("id", id).
                append("name", name).
//                append("user", user).
                toString();
    }
}
