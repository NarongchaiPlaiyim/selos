package com.clevel.selos.model.db.master;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "mst_role")
public class Role implements Serializable {
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "name", length = 20)
    private String name;
    @Column(name = "description", length = 100)
    private String description;
    @Column(name = "system_name", length = 20)
    private String systemName;
    @OneToOne
    @JoinColumn(name = "roletype_id")
    private RoleType roleType;
    @Column(name = "active")
    private int active;

    public Role() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public RoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
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
                append("name", name).
                append("description", description).
                append("systemName", systemName).
                append("roleType", roleType).
//                append("stepList", stepList).
                append("active", active).
                toString();
    }
}
