package com.clevel.selos.model.db.master;

import com.clevel.selos.model.RoleTypeName;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "mst_role_type")
public class RoleType implements Serializable {
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "name", length = 20)
    @Enumerated(EnumType.STRING)
    private RoleTypeName roleTypeName;
    @Column(name = "description", length = 50)
    private String description;
    @Column(name = "active")
    private int active;

    public RoleType() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public RoleTypeName getRoleTypeName() {
        return roleTypeName;
    }

    public void setRoleTypeName(RoleTypeName name) {
        this.roleTypeName = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
                append("roleTypeName", roleTypeName).
                append("description", description).
                append("active", active).
                toString();
    }
}
